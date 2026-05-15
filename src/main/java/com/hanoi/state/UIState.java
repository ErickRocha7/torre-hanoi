package com.hanoi.state;

import com.hanoi.config.Tema;
import com.hanoi.model.Disco;
import com.hanoi.model.DiscoId;
import java.util.List;
import java.util.Map;

public record UIState(
        List<List<Disco>> torres,
        Integer torreSelecionadaId,
        int movimentosAtuais,
        int movimentosMinimos,
        Tema tema,
        Map<DiscoId, EspaçoCartesiano> posicoes,
        boolean vitoria) {
    public record EspaçoCartesiano(double x, double y) {
    }
}
