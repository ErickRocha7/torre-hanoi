package com.hanoi.config;

public record LayoutConfig(
    double boardWidth,
    double boardHeight,
    double discoHeight,
    double discoWidthBase,
    double discoWidthIncrement,
    double pinoY,
    double pinoHeight,
    double pinoWidth
) {
    public static final LayoutConfig DEFAULT = new LayoutConfig(1200.0, 600.0, 24.0, 40.0, 20.0, 160.0, 350.0, 16.0);
}
