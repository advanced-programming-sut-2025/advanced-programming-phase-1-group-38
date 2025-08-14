package io.github.StardewValley.models;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

/**
 * Thread-safe, LibGDX-JSON-backed user store.
 * Stored shape: Map<String username, UserRecord>
 */
public final class SavaToJson {
    private static final String FILE_NAME = "users.json";
    private static final Json JSON = new Json();
    private static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock(true);

    // in-memory cache of users
    private static Map<String, UserRecord> CACHE = new HashMap<>();

    static {
        JSON.setOutputType(JsonWriter.OutputType.json);
        loadIntoCache();
    }

    private SavaToJson() {}

    // ---------------------------------------------------------------------
    // Public API
    // ---------------------------------------------------------------------

    /** Create a new user; throws if username already exists. */
    public static void registerUser(User u) {
        Objects.requireNonNull(u, "user");
        String uname = safe(u.getUsername());
        if (uname.isEmpty()) throw new IllegalArgumentException("Username required");
        LOCK.writeLock().lock();
        try {
            if (CACHE.containsKey(uname)) throw new IllegalStateException("User exists");
            CACHE.put(uname, toRecord(u));
            persist();
        } finally {
            LOCK.writeLock().unlock();
        }
    }

    /** True if a user with this username exists. */
    public static boolean userExists(String username) {
        LOCK.readLock().lock();
        try {
            return CACHE.containsKey(safe(username));
        } finally {
            LOCK.readLock().unlock();
        }
    }

    /** Return a copy of the user (password field contains the stored hash). */
    public static User getUser(String username) {
        LOCK.readLock().lock();
        try {
            UserRecord r = CACHE.get(safe(username));
            return r == null ? null : fromRecord(r);
        } finally {
            LOCK.readLock().unlock();
        }
    }

    /** Verify login using the raw password. */
    public static boolean verifyLogin(String username, String rawPassword) {
        LOCK.readLock().lock();
        try {
            UserRecord r = CACHE.get(safe(username));
            if (r == null) return false;
            return HashUtil.sha256(safe(rawPassword)).equals(r.passwordHash);
        } finally {
            LOCK.readLock().unlock();
        }
    }

    /** Returns the stored security question text (null if none). */
    public static String getSecurityQuestion(String username) {
        LOCK.readLock().lock();
        try {
            UserRecord r = CACHE.get(safe(username));
            return (r == null || r.securityQuestion == null || r.securityQuestion.isEmpty())
                ? null : r.securityQuestion;
        } finally {
            LOCK.readLock().unlock();
        }
    }

    /** Case-insensitive check of the answer (stored as sha256 of trimmed lowercase). */
    public static boolean verifySecurityAnswer(String username, String rawAnswer) {
        LOCK.readLock().lock();
        try {
            UserRecord r = CACHE.get(safe(username));
            if (r == null) return false;
            String given = HashUtil.sha256(safe(rawAnswer).trim().toLowerCase());
            return given.equals(r.securityAnswerHash);
        } finally {
            LOCK.readLock().unlock();
        }
    }

    /** Update password; accepts RAW or HASH (64 hex). */
    public static void updatePassword(String username, String passwordHashOrRaw) {
        LOCK.writeLock().lock();
        try {
            UserRecord r = CACHE.get(safe(username));
            if (r == null) throw new IllegalStateException("No such user: " + username);
            r.passwordHash = looksSha256(passwordHashOrRaw)
                ? passwordHashOrRaw
                : HashUtil.sha256(safe(passwordHashOrRaw));
            persist();
        } finally {
            LOCK.writeLock().unlock();
        }
    }

    /** Update non-credential profile fields (nickname/email/gender/stats/question-text). */
    public static void updateUser(User u) {
        Objects.requireNonNull(u, "user");
        String uname = safe(u.getUsername());
        LOCK.writeLock().lock();
        try {
            UserRecord r = CACHE.get(uname);
            if (r == null) throw new IllegalStateException("No such user: " + uname);

            // DO NOT touch passwordHash here (use updatePassword for that)
            r.nickname = safe(u.getNickname());
            r.email    = safe(u.getEmail());
            r.gender   = (u.getGender() == null) ? "RATHER_NOT_SAY" : u.getGender().name();

            if (u.getSecurityQuestion() != null) {
                // Allow changing the question text; DO NOT overwrite answer hash here.
                r.securityQuestion = safe(u.getSecurityQuestion().getQuestion());
            }

            r.highestGold      = u.getHighestGold();
            r.totalGamesPlayed = u.getTotalGamesPlayed();

            persist();
        } finally {
            LOCK.writeLock().unlock();
        }
    }

    /** Atomic rename of a username key (does not rehash or re-register). */
    public static void renameUser(String oldUsername, String newUsername) {
        String oldU = safe(oldUsername);
        String newU = safe(newUsername);
        if (newU.isEmpty()) throw new IllegalArgumentException("New username required");

        LOCK.writeLock().lock();
        try {
            if (!CACHE.containsKey(oldU)) throw new IllegalStateException("No such user: " + oldU);
            if (CACHE.containsKey(newU))  throw new IllegalStateException("User already exists: " + newU);
            UserRecord r = CACHE.remove(oldU);
            r.username = newU;
            CACHE.put(newU, r);
            persist();
        } finally {
            LOCK.writeLock().unlock();
        }
    }

    /** Delete a user by username (no-op if missing). */
    public static void deleteUser(String username) {
        LOCK.writeLock().lock();
        try {
            CACHE.remove(safe(username));
            persist();
        } finally {
            LOCK.writeLock().unlock();
        }
    }

    /** Convenience wrapper used by some UI code; persists current cache. */
    public static void save() {
        LOCK.writeLock().lock();
        try {
            persist();
        } finally {
            LOCK.writeLock().unlock();
        }
    }

    /** Snapshot list (for admin/debug). */
    public static List<User> listUsers() {
        LOCK.readLock().lock();
        try {
            ArrayList<User> out = new ArrayList<>(CACHE.size());
            for (UserRecord r : CACHE.values()) out.add(fromRecord(r));
            return out;
        } finally {
            LOCK.readLock().unlock();
        }
    }

    // ---------------------------------------------------------------------
    // Persistence
    // ---------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private static void loadIntoCache() {
        LOCK.writeLock().lock();
        try {
            FileHandle fh = Gdx.files.local(FILE_NAME);
            if (fh.exists()) {
                // LibGDX Json can parse a Map<String,UserRecord> when given element type
                Map<?, ?> raw = JSON.fromJson(HashMap.class, UserRecord.class, fh);
                CACHE = new HashMap<>();
                if (raw != null) {
                    for (Map.Entry<?, ?> e : raw.entrySet()) {
                        Object k = e.getKey();
                        Object v = e.getValue();
                        if (k instanceof String && v instanceof UserRecord) {
                            CACHE.put((String) k, (UserRecord) v);
                        }
                    }
                }
            } else {
                CACHE = new HashMap<>();
            }
        } catch (Exception e) {
            CACHE = new HashMap<>();
        } finally {
            LOCK.writeLock().unlock();
        }
    }

    private static void persist() {
        FileHandle fh = Gdx.files.local(FILE_NAME);
        // prettyPrint produces a string; overwrite file
        fh.writeString(JSON.prettyPrint(CACHE), false, "UTF-8");
    }

    // ---------------------------------------------------------------------
    // Mapping (DTO <-> domain)
    // ---------------------------------------------------------------------

    /** Stored record; add new fields with defaults for forward compatibility. */
    private static class UserRecord {
        String username;
        String passwordHash;          // SHA-256 hex
        String nickname;
        String email;
        String gender;                // enum name
        String securityQuestion;      // text (or id), answer is stored hashed
        String securityAnswerHash;    // SHA-256(answer.trim().toLowerCase())
        int highestGold;
        int totalGamesPlayed;
    }

    private static UserRecord toRecord(User u) {
        UserRecord r = new UserRecord();
        r.username = safe(u.getUsername());

        // Avoid double-hashing: your RegisterController already passes a SHA-256 hash.
        String pw = safe(u.getPassword());
        r.passwordHash = looksSha256(pw) ? pw : HashUtil.sha256(pw);

        r.nickname = safe(u.getNickname());
        r.email    = safe(u.getEmail());
        r.gender   = (u.getGender() == null) ? "RATHER_NOT_SAY" : u.getGender().name();

        if (u.getSecurityQuestion() != null) {
            r.securityQuestion   = safe(u.getSecurityQuestion().getQuestion());
            // store only the hash of the answer (trim+lowercase)
            r.securityAnswerHash = HashUtil.sha256(safe(u.getSecurityQuestion().getAnswer()).trim().toLowerCase());
        }

        r.highestGold      = u.getHighestGold();
        r.totalGamesPlayed = u.getTotalGamesPlayed();
        return r;
    }

    private static User fromRecord(UserRecord r) {
        User u = new User(
            r.username,
            r.passwordHash,  // store hash in User.password field (your code expects that)
            r.nickname,
            r.email,
            io.github.StardewValley.models.enums.Gender.valueOf(
                r.gender == null || r.gender.isEmpty() ? "RATHER_NOT_SAY" : r.gender)
        );
        u.setHighestGold(r.highestGold);
        u.setTotalGamesPlayed(r.totalGamesPlayed);
        if (r.securityQuestion != null) {
            // keep answer hidden in-memory; UI can display question text
            u.setSecurityQuestion(r.securityQuestion, "<hidden>");
        }
        return u;
    }

    private static String safe(String s) { return s == null ? "" : s; }

    private static boolean looksSha256(String s) {
        return s != null && s.matches("^[a-fA-F0-9]{64}$");
    }
}
