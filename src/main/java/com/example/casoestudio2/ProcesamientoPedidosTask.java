package com.example.casoestudio2;

import javafx.concurrent.Task;

public class ProcesamientoPedidosTask extends Task<Integer> {

    public static final int TOTAL_PEDIDOS = 30;
    private static final long TIEMPO_ESPERA_MS = 200L;

    @Override
    protected Integer call() throws Exception {
        int pedidosCompletados = 0;

        for (int i = 1; i <= TOTAL_PEDIDOS; i++) {
            // Verificación cooperativa antes de iniciar el pedido
            if (isCancelled()) {
                break;
            }

            try {
                // Simulación de trabajo en segundo plano
                Thread.sleep(TIEMPO_ESPERA_MS);
            } catch (InterruptedException e) {
                // Si la interrupción ocurre durante la espera, salimos sin sumar el pedido
                if (isCancelled()) {
                    break;
                }
                throw e;
            }

            // Se contabiliza únicamente tras terminar el procesamiento del pedido
            pedidosCompletados = i;
            updateProgress(pedidosCompletados, TOTAL_PEDIDOS);
            updateMessage(pedidosCompletados + " / " + TOTAL_PEDIDOS);
        }

        return pedidosCompletados;
    }
}