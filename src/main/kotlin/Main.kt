package org.example

import kotlin.system.exitProcess

data class Weapon(val name: String, val damage: Int)

val DEFAULT_WEAPON = Weapon("Stick", 7)

abstract class Enemy(val defaultHealth: Int) {
    private var _health: Int = defaultHealth

    abstract val name: String
    abstract val damage: Int

    val health: Int get() = _health

    fun hit(damage: Int) {
        _health -= damage
    }
}
// Bunny - 1damage, Bird - 1damage
class Slime: Enemy(8) {
    override val name: String get() = "Slime"
    override val damage: Int get() = 4
}

class Zombie: Enemy(12) {
    override val name: String get() = "Zombie"
    override val damage: Int get() = 6
}

class Bunny: Enemy(4) {
    override val name: String get() = "Bunny"
    override val damage: Int get() = 1
}

class Bird: Enemy(2) {
    override val name: String get() = "Bird"
    override val damage: Int get() = 1
}

val PLAYER_MAX_HP = 100
val PLAYER_SLEEP_HP = 50

class Player {
    private var _health: Int = PLAYER_MAX_HP
    private var _weapon: Weapon = DEFAULT_WEAPON

    val health: Int get() = _health
    val weapon: Weapon get() = _weapon

    fun hit(damage: Int) {
        _health -= damage
    }

    fun regenerate(): Int {
        val initialHp = _health
        _health += PLAYER_SLEEP_HP
        if (_health > PLAYER_MAX_HP)
            _health = PLAYER_MAX_HP

        return _health - initialHp
    }
}

val player = Player()

fun getRandomEnemy(): Enemy {
    val enemies = listOf(Slime(), Zombie(), Bird(), Bunny())
    return enemies.random()
}

fun startFight(enemy: Enemy) {
    while(enemy.health > 0 && player.health > 0) {
        println("Враг: ${enemy.health} HP, ${enemy.damage} DMG")
        println("Вы: ${player.health} HP, ${player.weapon.damage} DMG")
        println("")

        println("r - бежать, a - аттаковать")

        val op = readln()
        when(op) {
            "r" -> {
                player.hit(enemy.damage)
                println("Попытка сбежать...")
                break
            }

            "a" -> {
                enemy.hit(player.weapon.damage)
                player.hit(enemy.damage)
                println("УДАР!")
            }
        }
    }

    if(enemy.health <= 0) {
        println("Вы победили.")
    } else if(player.health <= 0) {
        println("ВЫ УМЕРЛИ!")
        exitProcess(1)
    }
}

// s - Спать, i - Инвентарь, x - Уйти в лес.
// '"{}|
fun goToHome() {
    while(true) {
        println("Вы дома.")
        println("s - Спать, i - Инвентарь, x - Уйти в лес.")
        val op = readln()
        when (op) {
            "s" -> {
                val addedHp = player.regenerate()
                println("Вы поспали. +${addedHp} HP")
            }
            "i" -> {
                println("У вас в руках ${player.weapon.name} (⚔️ ${player.weapon.damage})")

            }
            "x" -> return
        }
    }
}

fun main() {
    while(true) {
        println("Вы оказались в лесу.")
        println("Ваше здоровье: ${player.health} HP")

        val enemy = getRandomEnemy()

        println("Перед вами ${enemy.name}!")
        startFight(enemy)

        println("w - идти дальше, h - пойти домой")
        val op = readln()
        when(op) {
            "w" -> { continue }
            "h" -> { goToHome() }
        }
    }
}