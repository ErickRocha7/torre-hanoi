package com.hanoi.view;

import javafx.animation.Animation;
import javafx.application.Platform;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RenderScheduler {
    private final ConcurrentLinkedQueue<Animation> queue = new ConcurrentLinkedQueue<>();
    private Animation animacaoCorrente = null;

    public synchronized void agendar(Animation animacao) {
        queue.add(animacao);
        Platform.runLater(this::processarProximo);
    }

    private synchronized void processarProximo() {
        if (animacaoCorrente != null && animacaoCorrente.getStatus() == Animation.Status.RUNNING) {
            return;
        }

        Animation proxima = queue.poll();
        if (proxima != null) {
            this.animacaoCorrente = proxima;
            this.animacaoCorrente.setOnFinished(e -> {
                synchronized (this) {
                    this.animacaoCorrente = null;
                }
                processarProximo();
            });
            this.animacaoCorrente.play();
        }
    }
}
