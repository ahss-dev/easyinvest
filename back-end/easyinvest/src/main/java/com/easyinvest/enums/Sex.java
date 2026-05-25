package com.easyinvest.enums;

public enum Sex {

    MASCULINO ("Masculino"),
    FEMININO ("Feminino"),
    NAO_INFORMADO ("Prefiro não informar");

    private final String description;

    Sex (String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}