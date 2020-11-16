bayes-dota
==========

This is the [task](TASK.md).

#### Implementation

The information that has been parsed and processed from the combatlog are as follows:

* **Match**: When the application has been started an initial `Match` entity has been created with an `id`.
* **Hero**: After the, we gather which heroes are inside the game by going through the combat log. This is done because there may be a case where a hero didn't kill in a single game. We don't want to lose this data and return 0 for that hero and that is why we can't solely rely on `Kill` entity. Hero data has been persisted via `Hero` entity.
* **Hero Kills**: The only kill data that has been parsed are when the heroes are killing each other. When heroes have been killed by creeps, towers or creeps killed by heroes then it is not parsed. This data has been persisted via `Kill` entity.
* **Item Purchase**: The items that have been purchased by the heroes. This data has been perissted via `Item` entity.
* **Spells**: All the spells that has been cast throughout the game. Spells have been determined by the signals `casts ability` and `hits`. When a hero hits another one with a spell the line goes with the synonim `{hero_1} hits {hero_2} with {hero_1_spell_name}`. This is how we define that an hero attacked with a spell. Heals have been dismissed. Spell data has been persisted via `Spell` entity.
* **Damage**: The damage that has been to heroes from other heroes. All the other data have been dismissed.     

#### Notes

* In a real-world scenario, all the heroes in games like Dota or League of Legends are pre-defined. So it is better to use this pre-determined data and then use foreign keys inside other tables. However, the hero data in this challenge has been gathered throughout the combat logs.
* The hero called `Rubick` has a spell called `spell steal` which enables to cast other heroes' spells. Heads up!
  