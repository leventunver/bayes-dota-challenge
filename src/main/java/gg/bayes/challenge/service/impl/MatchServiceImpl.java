package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.entity.*;
import gg.bayes.challenge.parser.*;
import gg.bayes.challenge.repository.*;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final HeroRepository heroRepository;
    private final DamageRepository damageRepository;
    private final ItemRepository itemRepository;
    private final KillRepository killRepository;
    private final SpellRepository spellRepository;
    private final MatchRepository matchRepository;

    @Override
    @Transactional
    public Long ingestMatch(String payload) {
        String lines[] = payload.split("\\r?\\n");
        Match match = matchRepository.save(new Match());
        findHeroes(lines, match);
        for (String line : lines) {
            processLine(line, match);
        }
        return match.getId();
    }

    @Override
    public List<HeroKills> getMatch(Long matchId) {
        List<Hero> heroList = heroRepository.findAllByMatchId(matchId);
        List<HeroKills> heroKillsList = new ArrayList<>();
        for (Hero hero : heroList) {
            HeroKills heroKills = new HeroKills();
            heroKills.setHero(hero.getHeroName());
            heroKills.setKills(hero.getKillCount());
            heroKillsList.add(heroKills);
        }
        return heroKillsList;
    }

    @Override
    public List<HeroItems> getItems(Long matchId, String heroName) {
        List<Item> itemList = itemRepository.findAllByMatchIdAndHeroName(matchId, heroName);
        List<HeroItems> heroItemsList = new ArrayList<>();
        for (Item item : itemList) {
            HeroItems heroItems = new HeroItems();
            heroItems.setItem(item.getItemName());
            heroItems.setTimestamp(item.getTimestamp());
            heroItemsList.add(heroItems);
        }
        return heroItemsList;
    }

    @Override
    public List<HeroSpells> getSpells(Long matchId, String heroName) {
        List<SpellCount> spellCountList = spellRepository.findAllSpellCountByMatchIdandHeroName(matchId, heroName);
        List<HeroSpells> heroSpellsList = new ArrayList<>();
        for (SpellCount spellCount : spellCountList) {
            HeroSpells heroSpells = new HeroSpells();
            heroSpells.setSpell(spellCount.getSpellName());
            heroSpells.setCasts(spellCount.getSpellCount());
            heroSpellsList.add(heroSpells);
        }
        return heroSpellsList;
    }

    @Override
    public List<HeroDamage> getDamage(Long matchId, String heroName) {
        List<DamageCount> damageCountList = damageRepository.findAllDamageCountAndSumByMatchIdAndHeroName(matchId, heroName);
        List<HeroDamage> heroDamageList = new ArrayList<>();
        for (DamageCount damageCount : damageCountList) {
            HeroDamage heroDamage = new HeroDamage();
            heroDamage.setDamageInstances(damageCount.getDamageCount());
            heroDamage.setTarget(damageCount.getTarget());
            heroDamage.setTotalDamage(damageCount.getDamageSum());
            heroDamageList.add(heroDamage);
        }
        return heroDamageList;
    }

    private void findHeroes(String[] lines, Match match) {
        EventParser eventParser = new EventParser();
        Set<String> heroSet = new HashSet<>();
        for (String line : lines) {
            if (heroSet.size() == 10)
                break;
            String heroName = eventParser.getHeroName(line);
            if (heroName != null && !heroSet.contains(heroName)) {
                Hero hero = Hero.builder()
                        .heroName(heroName)
                        .match(match)
                        .killCount(0)
                        .build();
                heroRepository.save(hero);
                heroSet.add(heroName);
            }
        }
    }

    private void processLine(String line, Match match) {
        EventParser eventParser = new EventParser();

        EventType eventType = eventParser.getType(line);
        switch (eventType) {
            case PURCHASE:
                processPurchase(line, match);
                break;
            case KILL:
                processKill(line, match);
                break;
            case CAST:
                processCast(line, match);
                break;
            case DAMAGE:
                processDamage(line, match);
                break;
            case OTHER:
                break;
        }
    }

    private void processPurchase(String line, Match match) {
        EventParser eventParser = new EventParser();
        PurchaseEvent purchaseEvent = eventParser.getPurchaseEvent(line);
        if (purchaseEvent != null) {
            Item item = Item.builder()
                    .itemName(purchaseEvent.getItemName())
                    .heroName(purchaseEvent.getHeroName())
                    .match(match)
                    .timestamp(purchaseEvent.getTimestamp())
                    .build();
            itemRepository.save(item);
        }
    }

    private void processKill(String line, Match match) {
        EventParser eventParser = new EventParser();
        KillEvent killEvent = eventParser.getKillEvent(line);
        if (killEvent != null) {
            Kill kill = Kill.builder()
                    .heroName(killEvent.getHeroName())
                    .target(killEvent.getTarget())
                    .timestamp(killEvent.getTimestamp())
                    .match(match)
                    .build();
            killRepository.save(kill);
            heroRepository.incrementKillCount(match.getId(), killEvent.getHeroName());
        }
    }

    private void processDamage(String line, Match match) {
        EventParser eventParser = new EventParser();
        DamageEvent damageEvent = eventParser.getDamageEvent(line);
        if (damageEvent != null) {
            Damage damage = Damage.builder()
                    .damageAmount(damageEvent.getDamageAmount())
                    .heroName(damageEvent.getHeroName())
                    .hitBy(damageEvent.getHitBy())
                    .target(damageEvent.getTarget())
                    .timestamp(damageEvent.getTimestamp())
                    .match(match)
                    .build();
            damageRepository.save(damage);

            if (damageEvent.getSpellName() != null) {
                Spell spell = Spell.builder()
                        .spellName(damageEvent.getSpellName())
                        .heroName(damageEvent.getHeroName())
                        .target(damageEvent.getTarget())
                        .timestamp(damageEvent.getTimestamp())
                        .match(match)
                        .build();
                spellRepository.save(spell);
            }
        }
    }

    private void processCast(String line, Match match) {
        EventParser eventParser = new EventParser();
        CastEvent castEvent = eventParser.getCastEvent(line);
        if (castEvent != null) {
            Spell spell = Spell.builder()
                    .spellName(castEvent.getSpellName())
                    .heroName(castEvent.getHeroName())
                    .level(castEvent.getLevel())
                    .target(castEvent.getTarget())
                    .timestamp(castEvent.getTimestamp())
                    .match(match)
                    .build();
            spellRepository.save(spell);
        }
    }
}
