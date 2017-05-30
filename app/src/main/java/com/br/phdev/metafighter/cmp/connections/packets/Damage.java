package com.br.phdev.metafighter.cmp.connections.packets;

/**
 * @author Paulo Henrique Gonçalves Bacelar
 * @version 1.0
 */
public class Damage implements Packet {

    private float damage;

    public Damage(float damage){
        this.damage = damage;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }
}
