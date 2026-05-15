package com.hanoi.state;

import com.hanoi.config.LayoutConfig;
import com.hanoi.config.Tema;
import com.hanoi.model.Disco;
import com.hanoi.model.DiscoId;
import com.hanoi.model.Jogo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIStateDeriver {

    public static UIState derivar(Jogo jogo, Integer torreSelecionadaId, Tema tema, LayoutConfig cfg) {
        List<List<Disco>> torresEstrutura = new ArrayList<>();
        Map<DiscoId, UIState.EspaçoCartesiano> posicoes = new HashMap<>();
        double setorLargura = cfg.boardWidth() / 3.0;

        for (int i = 0; i < 3; i++) {
            var torreLogica = jogo.getTorres().get(i);
            List<Disco> discosDestaTorre = List.copyOf(torreLogica.getDiscos());
            torresEstrutura.add(discosDestaTorre);

            double centroX = (i * setorLargura) + (setorLargura / 2.0);

            for (int j = 0; j < discosDestaTorre.size(); j++) {
                Disco disco = discosDestaTorre.get(j);
                double w = cfg.discoWidthBase() + (disco.tamanho() * cfg.discoWidthIncrement());
                double x = centroX - (w / 2.0);
                double y = (cfg.boardHeight() - 60.0) - (j * (cfg.discoHeight() + 2.0));

                posicoes.put(disco.id(), new UIState.EspaçoCartesiano(x, y));
            }
        }

        return new UIState(
                torresEstrutura,
                torreSelecionadaId,
                jogo.getContadorMovimentos(),
                jogo.calcularMovimentosMinimos(),
                tema,
                Map.copyOf(posicoes),
                jogo.verificarCondConditionVitoria());
    }
}
