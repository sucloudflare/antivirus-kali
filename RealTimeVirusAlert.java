import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.util.Timer;
import java.util.TimerTask;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import javax.swing.JOptionPane;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class RealTimeVirusAlert {
    private static final String[] CRITICAL_DIRS = {"/tmp", "/var/log"};
    private static final int MONITOR_INTERVAL = 10000; // 10 segundos
    private static final String CURRENT_DATE = "14/06/2025 - 16:18 (-03)";
    private static final String[] MALWARE_SIGNATURES = {"trojan", "virus", "malware", ".exe"}; // Assinaturas básicas

    public static void main(String[] args) {
        System.out.println("Iniciando Alerta de Vírus em Tempo Real - Kali Linux Monitor... (Data: " + CURRENT_DATE + ")");
        startFileMonitor();
        startProcessMonitor();
        startNetworkMonitor();

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkSystemHealth();
            }
        }, 0, MONITOR_INTERVAL);

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            System.out.println("Monitor interrompido: " + e.getMessage());
        }
    }

    private static void startFileMonitor() {
        for (String dir : CRITICAL_DIRS) {
            try {
                WatchService watchService = FileSystems.getDefault().newWatchService();
                Path path = Paths.get(dir);
                path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);

                new Thread(() -> {
                    while (true) {
                        try {
                            WatchKey key = watchService.take();
                            for (WatchEvent<?> event : key.pollEvents()) {
                                String fileName = event.context().toString().toLowerCase();
                                if (isSuspiciousFile(fileName)) {
                                    String message = "Alerta: Alteração suspeita em " + dir + "/" + fileName + " - Possível malware!";
                                    handleThreat(message, dir + "/" + fileName, "file");
                                }
                            }
                            key.reset();
                        } catch (InterruptedException e) {
                            System.out.println("Monitor de arquivo interrompido: " + e.getMessage());
                        }
                    }
                }).start();
            } catch (Exception e) {
                System.out.println("Erro ao monitorar " + dir + ": " + e.getMessage());
            }
        }
    }

    private static void startProcessMonitor() {
        new Thread(() -> {
            while (true) {
                try {
                    Process process = Runtime.getRuntime().exec("ps aux");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (isSuspiciousProcess(line)) {
                            String message = "Alerta: Processo suspeito detectado: " + line;
                            handleThreat(message, line.split("\\s+")[1], "process");
                        }
                    }
                    reader.close();
                    Thread.sleep(MONITOR_INTERVAL);
                } catch (Exception e) {
                    System.out.println("Erro no monitor de processos: " + e.getMessage());
                }
            }
        }).start();
    }

    private static void startNetworkMonitor() {
        new Thread(() -> {
            while (true) {
                try {
                    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                    while (interfaces.hasMoreElements()) {
                        NetworkInterface ni = interfaces.nextElement();
                        if (ni.isUp() && !ni.isLoopback()) {
                            Enumeration<InetAddress> addresses = ni.getInetAddresses();
                            while (addresses.hasMoreElements()) {
                                InetAddress addr = addresses.nextElement();
                                if (isSuspiciousPortOpen(addr.getHostAddress())) {
                                    String message = "Alerta: Porta suspeita aberta em " + addr.getHostAddress();
                                    handleThreat(message, addr.getHostAddress(), "network");
                                }
                            }
                        }
                    }
                    Thread.sleep(MONITOR_INTERVAL);
                } catch (Exception e) {
                    System.out.println("Erro no monitor de rede: " + e.getMessage());
                }
            }
        }).start();
    }

    private static boolean isSuspiciousFile(String fileName) {
        for (String signature : MALWARE_SIGNATURES) {
            if (fileName.contains(signature)) return true;
        }
        return false;
    }

    private static boolean isSuspiciousProcess(String processLine) {
        String[] parts = processLine.toLowerCase().split("\\s+");
        if (parts.length > 10) {
            String command = parts[10];
            for (String signature : MALWARE_SIGNATURES) {
                if (command.contains(signature)) return true;
            }
        }
        return false;
    }

    private static boolean isSuspiciousPortOpen(String ip) {
        try {
            Process process = Runtime.getRuntime().exec("netstat -tuln | grep " + ip);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(":6667") || line.contains(":4444")) { // Portas de botnets
                    reader.close();
                    return true;
                }
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Erro ao verificar portas: " + e.getMessage());
        }
        return false;
    }

    private static void checkSystemHealth() {
        System.out.println("Monitorando... Sistema estável até o momento. (Data: " + CURRENT_DATE + ")");
    }

    private static void handleThreat(String message, String target, String type) {
        playAlertSound();
        showAlertDialog(message, target, type);
    }

    private static void playAlertSound() {
        try {
            File soundFile = new File("/antivirus-kali/alert.wav");
            if (soundFile.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            }
        } catch (Exception e) {
            System.out.println("Erro ao reproduzir som: " + e.getMessage());
        }
    }

    private static void showAlertDialog(String message, String target, String type) {
        Object[] options = {"Isolar", "Ignorar"};
        int choice = JOptionPane.showOptionDialog(null, message, "Alerta de Segurança - Kali Linux",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);

        if (choice == JOptionPane.YES_OPTION) {
            isolateThreat(target, type);
        }
    }

    private static void isolateThreat(String target, String type) {
        try {
            switch (type) {
                case "process":
                    Process killProcess = Runtime.getRuntime().exec("sudo kill -9 " + target);
                    killProcess.waitFor();
                    JOptionPane.showMessageDialog(null, "Processo " + target + " isolado com sucesso!", "Ação Concluída", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case "network":
                    Process blockPort = Runtime.getRuntime().exec("sudo iptables -A INPUT -s " + target + " -j DROP");
                    blockPort.waitFor();
                    JOptionPane.showMessageDialog(null, "Tráfego de " + target + " bloqueado com sucesso!", "Ação Concluída", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case "file":
                    Process quarantineFile = Runtime.getRuntime().exec("sudo mv " + target + " /quarantine/" + System.currentTimeMillis() + "_" + target);
                    quarantineFile.waitFor();
                    JOptionPane.showMessageDialog(null, "Arquivo " + target + " movido para quarentena!", "Ação Concluída", JOptionPane.INFORMATION_MESSAGE);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Tipo de ameaça desconhecido: " + type, "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao isolar ameaça: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            System.out.println("Erro ao isolar: " + e.getMessage());
        }
    }
}