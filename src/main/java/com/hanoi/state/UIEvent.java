package com.hanoi.state;

import com.hanoi.config.Tema;

public sealed interface UIEvent {
    record InteragirComTorre(int torreId) implements UIEvent {
    }

    record MudarTema(Tema novoTema) implements UIEvent {
    }

    record ResetarJogo() implements UIEvent {
    }
}
