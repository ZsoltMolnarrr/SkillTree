# 1.4.5

- Small tweaks to some arcane nodes

# 1.4.4

- Reworked "Conviction" passive now has internal cooldown

# 1.4.3

- Fixed spell nodes becoming non-functional after player death
- Fixed Improved Healing node also affecting the damage of Holy Shock #11

# 1.4.2

- Rework paladin passive "Conviction" to apply for Divine Protection

# 1.4.1

- Fixed Sword specialization not providing attribute bonus

# 1.4.0 - Weapon Skills rework

DISCLAIMER: Reworked skill ID, and node IDs!

- Added new skill tree tab to host independent weapon skills:
  - ID: `skill_tree_rpgs:weapon_skills`
  - 16 new skills
  - 1 static skill, and 1 choice out of 2, per weapon type
  - default point cap of 6
- Reworked skill tree tab ID:
  - `skill_tree_rpgs:skill_tree_rpgs` -> `skill_tree_rpgs:class_skills`
- New skill ID formulas
  - Spell modifiers: `class_tier_{tier}_spell_{choice}_modifier_{modifier}` - for example `archer_tier_3_spell_1_modifier_2`
  - Passive spells: `class_tier_{tier}_passive_{choice}` - for example `archer_tier_3_passive_1`
  - Also applies to skill tree node IDs
- Add conditional attributes feature
- Fixed missing cooldowns on some warrior passives
- Reworked warrior "Trample" skill, replaced by "Second Wind"

# 1.3.0

DISCLAIMER: All spell books and spell scrolls will be reset, due to major API changes. Some (looted) weapons with custom spell containers become non-functional, and need to be re-obtained. Apologies for the inconvenience.

- Update to use Spell Engine 1.9.0

# 1.2.3

- Fix triggers of Killing Spree
- Reduce volume of Arctic Reflex sound effect 

# 1.2.2

- Disable spell specific weakness
- Added Phase Shift cooldown

# 1.2.1

- Swap Sidestep and Leeching Strike positions in Rogue skill tree
- Increased radius of Trample 

# 1.2.0

- Skill Tree now loads if some of the class mods are missing 

# 1.1.1

- Add missing sound effects
- Update some passive spell particle effects 

# 1.1.0

- Vastly increased XP curve, maximal skill points increased to 11
- Fix skills not working after death
- Rework some of the existing skills
  - Rogue: Rupture renamed to Fracture, applies armor reduction instead damage taken increase
  - Warrior: Vitality now provides stacking Evasion Chance
- Add new passive skills for all classes
  - Arcane Wizard - Arcane Trap - Trap on roll
  - Arcane Wizard - Phase Shift - Brief invulnerability on roll
  - Arcane Wizard - Spell Riposte - Bolt on hit
  - Arcane Wizard - Arcane Ward - Shield on cast
  - Fire Wizard - Flame Trap - Roll leaves a trap that deals damage and applies Fire Vulnerability
  - Fire Wizard - Blazing Speed - Roll 50% chance grants temporary movement speed
  - Fire Wizard - Eruption - Taking damage 50% chance triggers an explosive AoE
  - Fire Wizard - Flame Shield - Fire spell casts may grant Fire Ward that absorbs damage and retaliates  - Frost Wizard - Frost Trap - Slow trap on roll
  - Frost Wizard - Frost Trap - Roll leaves a Frost Trap (5s) freezing first enemy (Freeze 6s, up to 4 stacks, 1.5m)
  - Frost Wizard - Arctic Reflex - Roll 25% chance grants 5s window for one instant Frost spell (10s cd)
  - Frost Wizard - Cold Snap - Taking damage 10% chance resets Frost spell cooldowns (30s cd)
  - Frost Wizard - Frost Shield - Frost spells 25% chance grant Frost Ward (absorbs damage, slows attackers, 8s ward, 16s cd)
  - Warrior - Intercept - Chance reset Charge on roll
  - Warrior - Trample - AoE damage while rolling
  - Warrior - Enrage - Buff on taking damage
  - Warrior - Shockwave - Stun near low HP hit
  - Archer - Momentum - Extra Rhythm on roll
  - Archer - Tactical Maneuver - Faster roll recharge chance
  - Archer - Supercharge - Empower next shot
  - Archer - Deflection - Parry buff on low HP hit
  - Priest - Fade - Roll makes nearby mobs drop aggro
  - Priest - Divine Favor - Big heal grants a brief damage absorb shield
  - Priest - Pain Suppression - Damage taken reduction for low HP targets
  - Priest - Celestial Orbs - Spell crits grant damaging orbs
  - Paladin - Crusader Strike - Roll empowers next melee to apply damage taken debuff
  - Paladin - Conviction - Consecutive hits build stacking damage buff
  - Paladin - Divine Hammer - Melee attacks launch a ricocheting hammer dealing spell damage (5s cooldown)
  - Paladin - Ardent Defender - Dropping below 30% HP (or fatal hit) grants temporary max health boost and heal (60s cooldown)
  - Rogue - Leeching Strike - Roll chance grants next melee life steal
  - Rogue - Sidestep - Roll grants stacking evasion; stacks removed on taking damage
  - Rogue - Cheat Death - Fatal blow prevented; brief invulnerability (cooldown)
  - Rogue - Preparation - Evade chance to reset all cooldowns
- New skill tree background :)
- Project now also has a fancy logo :)

# 1.0.3

- Fix technical implementation of skill node tooltip texts, Huge thanks to Pufferfish #1
- Fix background image fitment, Huge thanks to Pufferfish
- Update xp curve

# 1.0.2

- Fix xp curve

# 1.0.1

- Reduce trigger chance of Fissile Magic to 20%
- Reduce damage of Seal of Righteousness
- Update visuals of Seal of Righteousness

# 1.0.0

Initial release

# 