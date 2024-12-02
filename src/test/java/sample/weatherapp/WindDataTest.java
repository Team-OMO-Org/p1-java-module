package sample.weatherapp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import sample.weatherapp.models.WindData;

class WindDataTest {

  @Test
  void testConvertToDirWithCorrectData() {
    WindData windN = new WindData(14.48, 0);
    assertEquals("N", windN.convertToDirection());
    WindData windW = new WindData(15, 275);
    assertEquals("W", windW.convertToDirection());
    WindData windS = new WindData(15, 185);
    assertEquals("S", windS.convertToDirection());
    WindData windE = new WindData(15, 96);
    assertEquals("E", windE.convertToDirection());
    WindData wind = new WindData(7, 302);
    assertEquals("WNW", wind.convertToDirection());
    WindData windN1 = new WindData(7, 348.76);
    assertEquals("N", windN1.convertToDirection());
    WindData windN2 = new WindData(7, 360);
    assertEquals("N", windN2.convertToDirection());
    System.out.println(windN);
  }
}
