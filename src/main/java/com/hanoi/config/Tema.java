package com.hanoi.config;

public enum Tema {
    CATPPUCCIN_MACCHIATO(
            "#24273a", // Base / Fundo
            "#cad3f5", // Texto Principal
            "#eed49f", // Destaque / Seleção (Yellow)
            "#8bd5ca", // Torre Origem (Teal)
            "#91d7e3", // Torre Auxiliar (Sky)
            "#7dc4e4", // Torre Destino (Sapphire)
            "#f5bde6"// Vitória (Pink)
    );

    private final String background;
    private final String texto;
    private final String selecao;
    private final String pinoOrigem;
    private final String pinoAuxiliar;
    private final String pinoDestino;
    private final String vitoria;

    Tema(String background, String texto, String selecao, String pinoOrigem, String pinoAuxiliar, String pinoDestino,
            String vitoria) {
        this.background = background;
        this.texto = texto;
        this.selecao = selecao;
        this.pinoOrigem = pinoOrigem;
        this.pinoAuxiliar = pinoAuxiliar;
        this.pinoDestino = pinoDestino;
        this.vitoria = vitoria;
    }

    public String getBackground() {
        return background;
    }

    public String getTexto() {
        return texto;
    }

    public String getSelecao() {
        return selecao;
    }

    public String getPinoOrigem() {
        return pinoOrigem;
    }

    public String getPinoAuxiliar() {
        return pinoAuxiliar;
    }

    public String getPinoDestino() {
        return pinoDestino;
    }

    public String getVitoria() {
        return vitoria;
    }
}
