package controller;

import db.Conexao;
import util.LoggerUtil;
import util.TempoUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class AppController {
    private final Scanner scanner = new Scanner(System.in);
    private final String user = "admin";
    private final String senha = "admin123";
    private Connection conn;

    public void iniciar() {
        try {
            conn = Conexao.conectar();
            LoggerUtil.log("Aplicação iniciada.");
            TempoUtil.esperar(1000);

            if (login()) {
                dashboard();
            } else {
                LoggerUtil.log("Finalizando aplicação.");
            }

        } catch (Exception e) {
            LoggerUtil.log("Erro ao iniciar a aplicação: " + e.getMessage());
        }
    }

    private boolean login() throws InterruptedException {
        LoggerUtil.log("Tela de login");

        System.out.print("Usuário: ");
        String entradaUser = scanner.nextLine();

        System.out.print("Senha: ");
        String entradaSenha = scanner.nextLine();

        TempoUtil.esperar(1000);

        if (user.equals(entradaUser) && senha.equals(entradaSenha)) {
            LoggerUtil.log("Login realizado com sucesso.");
            return true;
        } else {
            LoggerUtil.log("Usuário ou senha inválidos.");
            return false;
        }
    }

    private void dashboard() throws Exception {
        boolean isRunning = true;
        while (isRunning) {
            LoggerUtil.log("Entrando na Dashboard...");

            System.out.println("\nEscolha uma opção:");
            System.out.println("1 - Registro de Auditoria");
            System.out.println("2 - Consultar Logs por Tipo");
            System.out.println("3 - Sair");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    listarLogs();
                    break;
                case 2:
                    consultarLogsPorTipo();
                    break;
                case 3:
                    isRunning = false;
                    LoggerUtil.log("Saindo da aplicação.");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private void listarLogs() throws Exception {
        String sql = "SELECT * FROM log";  // SELECT para listar todos os logs
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        LoggerUtil.log("Logs cadastrados:");

        while (rs.next()) {
            String tipo = rs.getString("tipo");
            String informacao = rs.getString("informacao");
            String descricao = rs.getString("descricao");
            LoggerUtil.log(" - Tipo: " + tipo + " | Informação: " + informacao + " | Descrição: " + descricao);
        }
        System.out.println("\nTodos logs listados.\n");
        rs.close();
        stmt.close();
    }

    private void consultarLogsPorTipo() throws Exception {
        System.out.print("Digite o tipo de log (INFO, ERROR, WARNING): ");
        String tipo = scanner.nextLine();

        String sql = "SELECT * FROM log WHERE tipo = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, tipo);
        ResultSet rs = stmt.executeQuery();

        LoggerUtil.log("Logs encontrados do tipo '" + tipo + "':");

        while (rs.next()) {
            String informacao = rs.getString("informacao");
            String descricao = rs.getString("descricao");
            LoggerUtil.log(" - Informação: " + informacao + " | Descrição: " + descricao);
        }

        System.out.println("\nTodos logs listados.\n");
        rs.close();
        stmt.close();
    }
}
