package com.hanoi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JogoTest {

    private Jogo jogo;

    @BeforeEach
    public void setUp() {
        // Inicializa um cenário padrão controlado com 3 discos para os testes
        jogo = new Jogo(3);
    }

    @Test
    public void testInicializacaoValida() {
        assertEquals(3, jogo.getQuantidadeTotalDiscos());
        assertEquals(0, jogo.getContadorMovimentos());
        assertEquals(7, jogo.calcularMovimentosMinimos(),
                "O cálculo matemático ideal para 3 discos deve ser 2^3 - 1 = 7");

        // Verifica se todos os 3 discos foram inseridos ordenadamente na torre de
        // origem
        assertEquals(3, jogo.getTorres().get(0).getQuantidadeDiscos());
        assertTrue(jogo.getTorres().get(1).estaVazia());
        assertTrue(jogo.getTorres().get(2).estaVazia());
    }

    @Test
    public void testQuantidadeDiscosInvalida() {
        assertThrows(IllegalArgumentException.class, () -> new Jogo(2), "Deve falhar se for menor que 3 discos");
        assertThrows(IllegalArgumentException.class, () -> new Jogo(9), "Deve falhar se for maior que 8 discos");
    }

    @Test
    public void testMovimentoValido() {
        // Move o disco menor (tamanho 1) da torre 0 (Origem) para a torre 2 (Destino)
        boolean moveu = jogo.validarEMover(0, 2);

        assertTrue(moveu, "O movimento inicial do topo deve ser válido");
        assertEquals(1, jogo.getContadorMovimentos());
        assertEquals(2, jogo.getTorres().get(0).getQuantidadeDiscos());
        assertEquals(1, jogo.getTorres().get(2).getQuantidadeDiscos());
        assertEquals(1, jogo.getTorres().get(2).espiarTopo().tamanho(),
                "O disco na torre destino deve ser o menor (tamanho 1)");
    }

    @Test
    public void testMovimentoInvalidoDiscoMaiorSobreMenor() {
        jogo.validarEMover(0, 2); // Move disco 1 para a torre 2

        // Tenta mover o disco 2 (agora no topo da torre 0) sobre o disco 1 (na torre 2)
        boolean moveuInvalido = jogo.validarEMover(0, 2);

        assertFalse(moveuInvalido, "Regra violada: Não deve ser permitido sobrepor um disco maior em cima de um menor");
        assertEquals(1, jogo.getContadorMovimentos(), "O contador de movimentos não deve incrementar em falhas");
    }

    @Test
    public void testMovimentoDeTorreVazia() {
        // Tenta mover a partir da torre 1, que inicia sem nenhum disco
        boolean moveuVazia = jogo.validarEMover(1, 2);

        assertFalse(moveuVazia, "Não deve ser permitido mover a partir de uma torre sem elementos");
    }

    @Test
    public void testCondicaoDeVitoria() {
        assertFalse(jogo.verificarCondConditionVitoria(), "Não deve iniciar em estado de vitória");

        // Executa a sequência matemática perfeita exata de 7 passos para resolver o
        // jogo de 3 discos
        jogo.validarEMover(0, 2); // 1 para 2
        jogo.validarEMover(0, 1); // 2 para 1
        jogo.validarEMover(2, 1); // 1 para 1
        jogo.validarEMover(0, 2); // 3 para 2
        jogo.validarEMover(1, 0); // 1 para 0
        jogo.validarEMover(1, 2); // 2 para 2
        jogo.validarEMover(0, 2); // 1 para 2

        assertTrue(jogo.verificarCondConditionVitoria(),
                "A vitória deve ser decretada quando todos os discos estiverem ordenados na torre destino");
    }
}
