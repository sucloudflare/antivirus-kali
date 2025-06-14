 <h1 class="text-center mb-4">💀 RealTimeVirusAlert - Monitor de Segurança em Tempo Real</h1>
        <div class="card p-4 mb-4">
            <h2>🚀 Descrição</h2>
            <p>O <strong>RealTimeVirusAlert</strong> é uma ferramenta desenvolvida em Java para Kali Linux, capaz de monitorar:</p>
            <ul>
                <li>🗂️ <strong>Arquivos</strong> em diretórios críticos (<code>/tmp</code>, <code>/var/log</code>)</li>
                <li>🔍 <strong>Processos</strong> suspeitos em execução</li>
                <li>🌐 <strong>Tráfego de Rede</strong> para portas maliciosas (ex.: 6667, 4444)</li>
                <li>📢 <strong>Alertas pop-up</strong> com opções: <strong>Isolar</strong> ou <strong>Ignorar</strong></li>
            </ul>
        </div>
    <div class="card p-4 mb-4">
            <h2>🔧 Pré-requisitos</h2>
            <ul>
                <li>☕ <strong>Java JDK 17+</strong></li>
                <li>🐧 <strong>Kali Linux</strong> (ou qualquer Linux com interface gráfica)</li>
                <li>🔑 Permissões <strong>root/sudo</strong></li>
                <li>🔊 Arquivo <code>alert.wav</code> (opcional para som)</li>
            </ul>
        </div>

   <div class="card p-4 mb-4">
            <h2>💾 Instalação</h2>
            <h5>1️⃣ Instale o Java</h5>
            <pre>sudo apt update
sudo apt install openjdk-17-jdk</pre>

  <h5>2️⃣ Clone o repositório</h5>
      <pre>git clone https://github.com/sucloudflare/antivirus-kali.git
cd antivirus-kali</pre>

   <h5>3️⃣ Crie a pasta de quarentena</h5>
       <pre>sudo mkdir /quarantine
sudo chmod 700 /quarantine</pre>

   <h5>4️⃣ (Opcional) Adicione o som de alerta</h5>
            <p>Coloque um arquivo <code>alert.wav</code> na raiz do projeto.</p>

   <h5>5️⃣ Compile o código</h5>
            <pre>javac RealTimeVirusAlert.java</pre>
        </div>

  <div class="card p-4 mb-4">
            <h2>⚙️ Uso</h2>
            <h5>Execute o programa</h5>
            <pre>sudo java RealTimeVirusAlert</pre>
            <p>🔔 Janelas pop-up aparecerão com opções de <strong>Isolar</strong> ou <strong>Ignorar</strong> ameaças detectadas.</p>

   <h5>Ações:</h5>
            <ul>
                <li>❌ <strong>Processos:</strong> Mata com <code>kill -9</code></li>
                <li>🔒 <strong>Rede:</strong> Bloqueia IP com <code>iptables</code></li>
                <li>📦 <strong>Arquivos:</strong> Move para <code>/quarantine</code></li>
            </ul>
        </div>

   <div class="card p-4 mb-4">
            <h2>🧪 Testes</h2>
            <ul>
                <li>📄 Arquivo: <code>touch /tmp/malware.txt</code> → Deve mover para <code>/quarantine</code></li>
                <li>💻 Processo: <pre>echo "malware" > /tmp/malware.sh
chmod +x /tmp/malware.sh
/tmp/malware.sh</pre> → Deve matar o processo</li>
                <li>🌐 Rede: <code>nc -l 6667</code> → Deve bloquear o IP</li>
            </ul>
        </div>

   <div class="card p-4 mb-4">
            <h2>🖥️ Rodar como Serviço (Opcional)</h2>
            <h5>Crie o arquivo de serviço:</h5>
            <pre>sudo nano /etc/systemd/system/virus-alert.service</pre>

 <h5>Conteúdo:</h5>
     <pre>[Unit]
Description=Real-Time Virus Alert Service
After=network.target

[Service]
ExecStart=/usr/bin/java -cp /home/bruno/RealTimeVirusAlert RealTimeVirusAlert
Restart=always
User=bruno

[Install]
WantedBy=multi-user.target</pre>

 <h5>Ative e inicie:</h5>
       <pre>sudo systemctl enable virus-alert.service
sudo systemctl start virus-alert.service</pre>
        </div>

   <div class="card p-4 mb-4">
            <h2>⚠️ Limitações</h2>
            <ul>
                <li>🧠 Detecção básica por assinaturas simples (ex.: "malware")</li>
                <li>🚧 Possíveis falsos positivos</li>
                <li>🖥️ Requer ambiente gráfico (X11/Wayland)</li>
            </ul>
        </div>

  <div class="card p-4 mb-4">
            <h2>🚀 Melhorias Futuras</h2>
            <ul>
                <li>🦠 Integração com ClamAV</li>
                <li>📧 Notificações por e-mail com <code>javax.mail</code></li>
                <li>🛡️ Monitoramento específico contra DDoS e ransomware</li>
            </ul>
        </div>

  <div class="card p-4 mb-4">
            <h2>🤝 Contribuições</h2>
            <p>✅ Abra uma <strong>issue</strong> ou envie um <strong>pull request</strong> com melhorias e sugestões!</p>

   <h2>📄 Licença</h2>
            <p>Este projeto está licenciado sob a <strong>MIT License</strong>.</p>

   <h2>🙏 Agradecimentos</h2>
            <p>Inspirado em ferramentas como <strong>Snort</strong> e <strong>iptables</strong>. Agradecimentos à comunidade Java e Kali Linux!</p>
  <p class="text-muted">Última atualização: 14/06/2025 - 16:34 (-03)</p>
      
