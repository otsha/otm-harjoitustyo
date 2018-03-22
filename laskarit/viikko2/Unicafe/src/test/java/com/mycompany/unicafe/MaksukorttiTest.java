package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void saldoMuuttujaAlussaOikein() {
        assertTrue(kortti.saldo()==10);
    }
    
    @Test
    public void saldoAlussaOikein() {
        assertEquals("saldo: 0.10", kortti.toString());
    }
    
    @Test
    public void rahanLataaminenKasvattaaSaldoaOikein() {
        kortti.lataaRahaa(150);
        assertEquals("saldo: 1.60", kortti.toString());
    }
    
    @Test
    public void saldoVaheneeOikeinJosRahaaOnTarpeeksi() {
        kortti.otaRahaa(9);
        assertEquals("saldo: 0.01", kortti.toString());
    }
    
    @Test
    public void saldoEiMuutuJosRahaaEiOleTarpeeksi() {
        kortti.otaRahaa(200);
        assertEquals("saldo: 0.10", kortti.toString());
    }
    
    @Test
    public void palautetaanFalseJosRahatEivatRiittaneet() {
        assertTrue(kortti.otaRahaa(200) == false);
    }
    
    
}
