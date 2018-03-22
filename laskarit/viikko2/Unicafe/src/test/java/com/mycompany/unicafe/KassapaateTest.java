/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.unicafe;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author haaotso
 */
public class KassapaateTest {

    Kassapaate kassa;

    @Before
    public void setUp() {
        kassa = new Kassapaate();
    }

    @Test
    public void kassapaatteenRahamaaraOnAluksiOikea() {
        assertEquals(1000, kassa.kassassaRahaa());
    }

    @Test
    public void lounaitaMyytyAluksiNolla() {
        assertEquals(0, kassa.edullisiaLounaitaMyyty() + kassa.maukkaitaLounaitaMyyty());
    }

    @Test
    public void josMaksuRiittavaKassassaOlevaRahamaaraKasvaaEdullisenLounaanHinnalla() {
        kassa.syoEdullisesti(240);
        assertEquals(1240, kassa.kassassaRahaa());
    }

    @Test
    public void josMaksuRiittavaKassassaOlevaRahamaaraKasvaaMaukkaanLounaanHinnalla() {
        kassa.syoMaukkaasti(400);
        assertEquals(1400, kassa.kassassaRahaa());
    }

    @Test
    public void oikeaVaihtorahaKunOstetaanEdullinenLounas() {
        assertEquals(10, kassa.syoEdullisesti(250));
    }

    @Test
    public void oikeaVaihtorahaKunOstetaanMaukasLounas() {
        assertEquals(20, kassa.syoMaukkaasti(420));
    }

    @Test
    public void myytyjenEdullistenMaaraKasvaaOikein() {
        kassa.syoEdullisesti(240);
        kassa.syoEdullisesti(240);
        assertEquals(2, kassa.edullisiaLounaitaMyyty());
    }

    @Test
    public void myytyjenMaukkaidenMaaraKasvaaOikein() {
        kassa.syoMaukkaasti(400);
        kassa.syoMaukkaasti(400);
        assertEquals(2, kassa.maukkaitaLounaitaMyyty());
    }

    @Test
    public void kassanRahamaaraEiMuutuJosMaksuEiRiittavaEdullinen() {
        kassa.syoEdullisesti(120);
        assertEquals(1000, kassa.kassassaRahaa());
    }

    @Test
    public void kassanRahamaaraEiMuutuJosMaksuEiRiittavaMaukas() {
        kassa.syoMaukkaasti(350);
        assertEquals(1000, kassa.kassassaRahaa());
    }

    @Test
    public void kaikkiRahatPalautetaanVaihtorahanaJosMaksuEiRiittavaEdullinen() {
        assertEquals(120, kassa.syoEdullisesti(120));
    }

    @Test
    public void kaikkiRahatPalautetaanVaihtorahanaJosMaksuEiRiittavaMaukas() {
        assertEquals(350, kassa.syoMaukkaasti(350));
    }

    @Test
    public void edullistenMaaraEiMuutuJosMaksuEiRiittava() {
        kassa.syoEdullisesti(120);
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
    }

    @Test
    public void maukkaidenMaaraEiMuutuJosMaksuEiRiittava() {
        kassa.syoMaukkaasti(350);
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
    }

    // -- Maksukorttia käyttävät operaatiot --
    @Test
    public void josKortillaOnTarpeeksiRahaaVeloitetaanEdullinenJaPalautetaanTrue() {
        Maksukortti kortti = new Maksukortti(1000);
        assertTrue(kassa.syoEdullisesti(kortti) == true);
        assertEquals(760, kortti.saldo());
    }

    @Test
    public void josKortillaOnTarpeeksiRahaaVeloitetaanMaukasJaPalautetaanTrue() {
        Maksukortti kortti = new Maksukortti(1000);
        assertTrue(kassa.syoMaukkaasti(kortti) == true);
        assertEquals(600, kortti.saldo());
    }

    @Test
    public void josKortillaOnTarpeeksiRahaaMyytyjenEdullistenMaaraKasvaa() {
        Maksukortti kortti = new Maksukortti(1000);
        kassa.syoEdullisesti(kortti);
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
    }

    @Test
    public void josKortillaOnTarpeeksiRahaaMyytyjenMaukkaidenMaaraKasvaa() {
        Maksukortti kortti = new Maksukortti(1000);
        kassa.syoMaukkaasti(kortti);
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void josKortillaEiOleTarpeeksiRahaaEdulliseenKortinRahamaaraEiMuutuLounaidenMaaraMuuttumatonJaPalautetaanFalse() {
        Maksukortti kortti = new Maksukortti(230);
        assertTrue(kassa.syoEdullisesti(kortti) == false);
        assertEquals(230, kortti.saldo());
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void josKortillaEiOleTarpeeksiRahaaMaukkaaseenKortinRahamaaraEiMuutuLounaidenMaaraMuuttumatonJaPalautetaanFalse() {
        Maksukortti kortti = new Maksukortti(230);
        assertTrue(kassa.syoMaukkaasti(kortti) == false);
        assertEquals(230, kortti.saldo());
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void kassassaOlevaRahamaaraEiMuutuKortillaOstettaessa() {
        Maksukortti kortti = new Maksukortti(1000);
        kassa.syoEdullisesti(kortti);
        assertEquals(1000, kassa.kassassaRahaa());
        kassa.syoMaukkaasti(kortti);
        assertEquals(1000, kassa.kassassaRahaa());
    }
    
    @Test
    public void kortilleLataaminenKasvattaaKortinJaKassanRahamaaria() {
        Maksukortti kortti = new Maksukortti(1000);
        kassa.lataaRahaaKortille(kortti, 500);
        assertEquals(1500, kortti.saldo());
        assertEquals(1500, kassa.kassassaRahaa());
    }
    
    @Test
    public void kortilleEiVoiLadataNegatiivistaArvoa() {
        Maksukortti kortti = new Maksukortti(1000);
        kassa.lataaRahaaKortille(kortti, -1500);
        assertEquals(1000, kortti.saldo());
        assertEquals(1000, kassa.kassassaRahaa());
    }

}
