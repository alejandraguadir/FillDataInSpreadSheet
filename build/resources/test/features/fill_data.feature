#Author: Yolima Alejandra Guadir

Feature: Fill Data in Spread Sheet
  yo como analista
  quiero llenar datos en una hoja de calculo
  para poder hacer pruebas

  @Compra
  Scenario: Fill Data in Spread Sheet
    Given the user upload the data
    When execute the action
    Then fill data on the sheet