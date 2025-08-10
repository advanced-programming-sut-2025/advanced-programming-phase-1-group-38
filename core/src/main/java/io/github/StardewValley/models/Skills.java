package io.github.StardewValley.models;

import io.github.StardewValley.models.enums.Skill;

import java.util.EnumMap;
import java.util.Map;

public class Skills {
    private final Map<Skill, SkillState> map = new EnumMap<>(Skill.class);
    public Skills() {
        for (Skill t : Skill.values()) map.put(t, new SkillState());
    }
    public SkillState get(Skill t) { return map.get(t); }
    public Map<Skill, SkillState> all() { return map; }
}