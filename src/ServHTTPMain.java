import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

//*********************************************************************************************************************
//                                                                                                                    *
// Autor: Antonio Bernardo de Vasconcellos Praxedes                                                                   *
//                                                                                                                    *  
// Data: 22/09/2022                                                                                                   *
//                                                                                                                    *
// Nome da Classe: ServHTTPMain                                                                                       *
//                                                                                                                    *
// Funcao: Programa Principal Servidor HTTP para operação na Intranet                                                 *
//                                                                                                                    *
//*********************************************************************************************************************
//
public class ServHTTPMain implements Runnable {
	
	private static boolean Verbose = true;
	private static String Caminho = "";
	private static String CaminhoRaspberryPI = "/home/pi/Desktop/Programas/";
	private static String CaminhoDellVostro = "/home/antonio/Workspace/Recursos/";
		
	private Socket connect;
	
	String IPConcArd = "192.168.0.150";
    int PortaUDP = 5683;
				
	public ServHTTPMain(Socket c) {
		connect = c;
	}

	//*****************************************************************************************************************
	//                                                                                                                *
    // Método Executavel da ClasseHTTPSrvSup                                                                          *
	//                                                                                                                *
	// Funcao: Servidor HTTP aguarda a conexão do Cliente                                                             *
	//                                                                                                                *
	//*****************************************************************************************************************
	//
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int Porta = 8080;
				
		try {
			ServerSocket serverConnect = new ServerSocket(Porta);
			InetAddress ip = InetAddress.getLocalHost();
			String NomeComputador = "";
			NomeComputador = ip.getHostName();
			
			if (NomeComputador.equals("antonio-Vostro1510")) {
				Caminho = CaminhoDellVostro;
				Util.Terminal("Servidor Iniciado no Computador Vostro-1510", false, true);
			}
			else {
				Caminho = CaminhoRaspberryPI;
				Util.Terminal("Servidor Iniciado no Computador Raspberry PI", false, true);
			}
			
			Util.Terminal("Esperando por Conexoes na Porta: " + Porta, false, Verbose);
			
			while (true) {    // Espera a conexão do cliente
				ServHTTPMain myServer = new ServHTTPMain(serverConnect.accept());
				Util.Terminal("Conexao Aberta com o Cliente (" + new Date() + ")", false, Verbose);
				Thread thread = new Thread(myServer);      // Thread para gerenciar a conexão do cliente
				thread.start();
			}
		} catch (IOException e) {
			System.err.println("Erro na Conexao com o Servidor: " + e.getMessage());
		}
	} // Fim da Rotina public static void main(String[] args) {
	
	
	//***************************************************************************************************************************
	//                                                                                                                          *
    // Processa a Solicitação do Cliente                                                                                        *
	//                                                                                                                          *
	// Funcao: processa a solicitação do Cliente HTTP                                                                           *
	//                                                                                                                          *
	//***************************************************************************************************************************
	//
	//@Override
	public void run() {
		
		BufferedReader in = null; PrintWriter out = null; BufferedOutputStream dataOut = null;
		InputStreamReader dataIn = null; InputStream ByteIn = null; //String fileRequested = null;
						
		try {
			ByteIn = connect.getInputStream();
			dataIn = new InputStreamReader(ByteIn); //
			in = new BufferedReader(dataIn);  //new InputStreamReader(connect.getInputStream()));
			out = new PrintWriter(connect.getOutputStream());
			dataOut = new BufferedOutputStream(connect.getOutputStream());
						
		    int ContMsgCoAP = 0;
			int Comando = 0;
			
			String LinhaCab[] = new String[12];
			int CChar = 0;
			int CLin = 0;
			int ChRec = 0;
			int CR = 13;
			int LF = 10;
			boolean Leu_CRLF = false;
			boolean fim = false;
			String Requisicao = null;
			LinhaCab[0] = "";
			boolean mobile = false;
									
			while (!fim) {
				ChRec = ByteIn.read();
				CChar = CChar + 1;
				if (CChar > 2000) { fim = true; }
				if (ChRec == CR) {
					ChRec = ByteIn.read();
					if (ChRec == LF) {
						if (Leu_CRLF) {
							fim = true;
						}
						else {
							LinhaCab[CLin] = LinhaCab[CLin] + "\n";
							CLin = CLin + 1;
							LinhaCab[CLin] = "";
							Leu_CRLF = true;
						}
					}
				}
				else {
					LinhaCab[CLin] = LinhaCab[CLin] + (char)ChRec;
					Leu_CRLF = false;
				}
			}	
					
			// Monta o Cabeçalho da Requisição na String CabHTTP
			String CabHTTP = "";
			for (int k = 0; k < CLin; k++){
				CabHTTP = CabHTTP + LinhaCab[k] + "\n";
				//System.out.println("k = " + k + " - Linha: " + LinhaCab[k]);
			}
			
			if (CabHTTP.toLowerCase().indexOf("mobile") >= 0) {
				mobile = true;
				System.out.println("Acesso por Dispositivo Móvel");
				Util.Terminal("Acesso por Dispositivo Móvel", false, Verbose);
			}
			else {
				mobile = false;
			}
			
			StringTokenizer parseLinha1 = new StringTokenizer(LinhaCab[0]);
			String method = parseLinha1.nextToken().toUpperCase();
			String ArquivoReq = "";
			
			if (parseLinha1.hasMoreTokens()) {;
				Requisicao = parseLinha1.nextToken();
				ArquivoReq = Requisicao.substring(1);
			}
			
			int TamArqReq = ArquivoReq.length();
			String ArqReq = "";
			
			boolean RecMetodoValido = false;
			boolean RecReqValida = false;
			Util.Terminal("Método: " + method + "  -  Arquivo Requisitado: " + ArquivoReq, false, Verbose);
			
			if (method.equals("GET")) {  // Trata o método GET
				RecMetodoValido = true;
				
				// Se não há requisição de arquivo, solicita arquivo index.html (página raiz)
				if (Requisicao.equals("/") || Requisicao.equals("/?")) {
					RecReqValida = EnvRecMsg.EnvArqTxt(connect, Caminho, "index.html", Verbose);
				}
				else { // Trata a requisição do método GET
					
					// Trata requisições de arquivos texto de página HTML
					if (ArquivoReq.endsWith(".html")) {
						
						if (mobile) {
							ArqReq = ArquivoReq.substring(0, TamArqReq - 5);
							ArqReq = ArqReq + ".m.html"; 
							}
						else {
							ArqReq = ArquivoReq;
						}
						RecReqValida = EnvRecMsg.EnvArqTxt(connect, Caminho, ArqReq, Verbose);
					}
					
					// Trata requisições de arquivos texto de estilos (CSS)
					if (ArquivoReq.endsWith(".css")) {
						if (mobile) {
							ArqReq = ArquivoReq.substring(0, TamArqReq - 4);
							ArqReq = ArqReq + ".m.css"; 
							}
						else {
							ArqReq = ArquivoReq;
						}
						RecReqValida = EnvRecMsg.EnvArqTxt(connect, Caminho, ArqReq, Verbose);
					}
					
					// Trata requisições de arquivos de programas Javascript
					if (ArquivoReq.endsWith(".js")) {
						if (mobile) {
							ArqReq = ArquivoReq.substring(0, TamArqReq - 3);
							ArqReq = ArqReq + ".m.js"; 
							}
						else {
							ArqReq = ArquivoReq;
						}
						RecReqValida = EnvRecMsg.EnvArqTxt(connect, Caminho, ArqReq, Verbose);
					}
					
					// Trata requisições de arquivos de imagem
					if (ArquivoReq.endsWith(".ico") || ArquivoReq.endsWith(".jpg") || ArquivoReq.endsWith(".png")) {
						RecReqValida = EnvRecMsg.EnvArqByte(connect, Caminho, ArquivoReq, Verbose);
					}
						
					// Trata requisição de mensagem XML de Atualização dos Valores das Variáveis
					if (ArquivoReq.endsWith("local001.xml")) {
						RecReqValida = true;
								
						byte[] MsgCoapRec = EnvRecMsg.CoAPUDP(IPConcArd, PortaUDP, "estados", ContMsgCoAP, Comando, Verbose);
						
						if (MsgCoapRec.length > 1) {
							Mensagem.CarregaVariaveis(MsgCoapRec);
							EnvRecMsg.EnvString(connect, Mensagem.MontaXML(), "text/xml", "200", Verbose);
						}
						else {
							EnvRecMsg.EnvString(connect, Mensagem.MontaXMLFalha(0), "text/xml", "200", Verbose);
						}
					}
				}
			}
			
			// Se método = POST, é comando
			if (method.equals("POST")) {
				RecMetodoValido = true;
				String StrComando = ArquivoReq.substring(4);
				Util.Terminal("StrComando = " + StrComando, false, Verbose);
				if (StrComando.startsWith("cmd")) {
					RecReqValida = true;
					Comando = ExecComandoHTTP(StrComando, Verbose);
					byte[] MsgCoapRec = EnvRecMsg.CoAPUDP(IPConcArd, PortaUDP, "estados", ContMsgCoAP, Comando, Verbose);
					Mensagem.CarregaVariaveis(MsgCoapRec);
					EnvRecMsg.EnvString(connect, Mensagem.MontaXML(), "text/xml", "200", Verbose);
					Comando = 0;
				}
			}
			
			if (RecMetodoValido) {    // Se foi recebido um método válido,
				if (!RecReqValida) {  // e se não está disponível o recurso solicitado pelo método GET ou POST
					EnvRecMsg.EnvStringErro(connect, 404, Verbose);
				}
			}
			else {                    // Se não foi recebido um método válido,
				EnvRecMsg.EnvStringErro(connect, 501, Verbose);
			}
			
		} catch (IOException ioe) {
			Util.Terminal("Erro no Servidor: " + ioe, false, Verbose);
		} finally {
			try {
				in.close();
				out.close();
				dataOut.close();
				connect.close();
			} catch (Exception e) {
				Util.Terminal("Erro no fechamento do stream : " + e.getMessage(), false, Verbose);
			} 
			Util.Terminal("Conexao com o Cliente Encerrada", false, Verbose);
			
		}
	}
	
    //******************************************************************************************************************
    //                                                                                                                 *
    // Nome do Método: ExecComandoHTTP                                                                                 *
    //	                                                                                                               *
    // Funcao: obtém o número do comando referente à string de comando recebida do Servidor HTTP                       *
    //                                                                                                                 *
    // Entrada: string com o código do comando recebido do Servidor HTTP e a habilitação de impressão de mensagem      *
    //                                                                                                                 *
    // Saida: o número inteiro refetente à string de comando recebida do Servidor HTTP                                 *
    //	                                                                                                               *
    //******************************************************************************************************************
    //
    private static int ExecComandoHTTP(String comrechttp, boolean Verbose) {
        int Comando = 0;
        if (comrechttp.equals("cmd=0002")) {
            Comando = 2;
            Util.Terminal("Comando: Acerto Relogio", false, Verbose);
        }
        if (comrechttp.equals("cmd=0003")) {
            Comando = 3;
            Util.Terminal("Comando: Modo Economia", false, Verbose);
        }
        if (comrechttp.equals("cmd=0004")) {
            Comando = 4;
            Util.Terminal("Comando: Modo Normal", false, Verbose);
        }
        if (comrechttp.equals("cmd=0016")) {
            Comando = 16;
            Util.Terminal("Comando: Manual Carga 1", false, Verbose);
        }
        if (comrechttp.equals("cmd=0017")) {
            Comando = 17;
            Util.Terminal("Comando: Automatico Carga 1", false, Verbose);
        }
        if (comrechttp.equals("cmd=0005")) {
            Comando = 5;
            Util.Terminal("Comando: Manual Cargas 234", false, Verbose);
        }
        if (comrechttp.equals("cmd=0006")) {
            Comando = 6;
            Util.Terminal("Comando: Automatico Cargas 234", false, Verbose);
        }
        if (comrechttp.equals("cmd=0007")) {
            Comando = 7;
            Util.Terminal("Comando: Habilita Carga 1", false, Verbose);
        }
        if (comrechttp.equals("cmd=0008")) {
            Comando = 8;
            Util.Terminal("Comando: Desabilita Carga 1", false, Verbose);
        }
        if (comrechttp.equals("cmd=0009")) {
            Comando = 9;
            Util.Terminal("Comando: Habilita Carga 2", false, Verbose);
        }
        if (comrechttp.equals("cmd=0010")) {
            Comando = 10;
            Util.Terminal("Comando: Desabilita Carga 2", false, Verbose);
        }
        if (comrechttp.equals("cmd=0011")) {
            Comando = 11;
            Util.Terminal("Comando: Habilita Carga 3", false, Verbose);
        }
        if (comrechttp.equals("cmd=0012")) {
            Comando = 12;
            Util.Terminal("Comando: Desabilita Carga 3", false, Verbose);
        }
        if (comrechttp.equals("cmd=0013")) {
            Comando = 13;
            Util.Terminal("Comando: Habilita Carga 4", false, Verbose);
        }
        if (comrechttp.equals("cmd=0014")) {
            Comando = 14;
            Util.Terminal("Comando: Desabilita Carga 4", false, Verbose);
        }
        if (comrechttp.equals("cmd=0015")) {
            Comando = 15;
            Util.Terminal("Comando: Apaga Indicadores de Falha", false, Verbose);
        }
        return(Comando);
    }
    
}

