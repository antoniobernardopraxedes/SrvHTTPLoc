import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

//*****************************************************************************************************************
//                                                                                                                *
// Nome da Classe: Arquivo                                                                                        *
//                                                                                                                *
// Métodos: Lê arquivo texto (sequência de caracteres), Lê arquivo binário (sequência de bytes), escreve          *
//          arquivo texto                                                                                         *
//                                                                                                                *
//*****************************************************************************************************************
//
public class Arquivo {

	boolean Existe(String Caminho, String NomeArquivo) {
		File Arquivo = new File(Caminho + NomeArquivo);
		return (Arquivo.exists());
	}
	
	int Tamanho(String Caminho, String NomeArquivo) {
		File Arquivo = new File(Caminho + NomeArquivo);
		return ((int)Arquivo.length());
	}
	
	//*****************************************************************************************************************
	// Nome do Método: LeTexto                                                                                        *
    //	                                                                                                              *
	// Funcao: lê um arquivo texto (sequência de caracteres)                                                          *
	//                                                                                                                *
	// Entrada: string com o nome do caminho e string com o nome do arquivo                                           *
	//                                                                                                                *
	// Saida (string): conteúdo do arquivo lido (se ocorrer erro na leitura, retorna uma string vazia                 *
    //	                                                                                                              *
	//*****************************************************************************************************************
	//
	String LeTexto(String Caminho, String NomeArquivo) {
		File Arquivo = new File(Caminho + NomeArquivo);
		String Arq = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(Arquivo));
			
			String st; 
			while ((st = br.readLine()) != null) {
				Arq = Arq + st + "\n";
			}
		} catch (IOException e) {
			return("");
		}
		return(Arq);
	}
	
	//*****************************************************************************************************************
	// Nome do Método: LeByte                                                                                         *
    //	                                                                                                              *
	// Funcao: lê um arquivo binário (sequência de bytes)                                                             *
	//                                                                                                                *
	// Entrada: string com o nome do caminho e string com o nome do arquivo                                           *
	//                                                                                                                *
	// Saida (array de bytes): conteúdo do arquivo lido (se ocorrer erro na leitura, retorna um array vazio           *
    //	                                                                                                              *
	//*****************************************************************************************************************
	//
	byte[] LeByte(String Caminho, String NomeArquivo) throws IOException {
		File Arquivo = new File(Caminho + NomeArquivo);
		FileInputStream ArquivoLido = null;
		int TamArquivo = (int)Arquivo.length();
		byte[] DadosArquivo = new byte[TamArquivo];
		
		try {
			ArquivoLido = new FileInputStream(Arquivo);
			ArquivoLido.read(DadosArquivo);
		} finally {
			if (ArquivoLido != null) 
				ArquivoLido.close();
		}
		return DadosArquivo;
	}
	
	
	//*****************************************************************************************************************
	// Nome do Método: EscTxt                                                                                         *
    //	                                                                                                              *
	// Funcao: escreve um arquivo texto                                                                               *
	//                                                                                                                *
	// Entrada: string com o nome do caminho, string com o nome do arquivo e string com o texto a ser escrito         *
	//                                                                                                                *
	// Saida (boolean): true: escreveu o arquivo / false: falha ao escrever o arquivo                                 *
    //	                                                                                                              *
	//*****************************************************************************************************************
	//
	public static boolean EscTxt(String Caminho, String NomeArquivo, String Texto, boolean Verbose) {

	try {
		PrintWriter out = new PrintWriter(new FileWriter(Caminho + NomeArquivo));
		out.write(Texto);
		out.close();
		return (true);
		} catch (IOException e) {
			if (Verbose) {
				System.out.print("Erro ao escrever arquivo texto ");
			}
			return (false);
		}
	}
	
	String Tipo(String NomeArquivo) {
		String tipo = "";
		
		if (NomeArquivo.endsWith(".htm")  ||  NomeArquivo.endsWith(".html")) {
			tipo = "text/html";
		}
		
		if (NomeArquivo.endsWith(".js")) {
			tipo = "text/javascript";
		}
		
		if (NomeArquivo.endsWith(".css")) {
			tipo = "text/css";
		}
		
		if (NomeArquivo.endsWith(".jpg")  ||  NomeArquivo.endsWith(".jpeg")) {
			tipo = "image/jpeg";
		}
		
		if (NomeArquivo.endsWith(".gif")) {
			tipo = "image/gif";
		}
		
		if (NomeArquivo.endsWith(".png")) {
			tipo = "image/png";
		}
		
		if (NomeArquivo.endsWith(".bmp")) {
			tipo = "image/bmp";
		}
		
		if (NomeArquivo.endsWith(".txt")) {
			tipo = "text/plain";
		}
		
		return(tipo);
	}
	
}
