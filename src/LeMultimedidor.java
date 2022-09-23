import java.io.IOException;
import java.net.InetAddress;
import java.text.DecimalFormat;


//*********************************************************************************************************************
//                                                                                                                    *                                                     *
// Projeto: Leitura e Parametrização do Multimedidor KRON MULT T K 05                                                 *
//                                                                                                                    *
// Nome da Classe: LeMultimedidor (executável)                                                                        *
//                                                                                                                    *
// Funcao: monta, envia uma mensagem para o Concentrador Arduino Uno, e espera a resposta em protocolo UDP            *
//                                                                                                                    *
//*********************************************************************************************************************
//
public class LeMultimedidor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		boolean Verbose = true;
		
		String IPConcArd = "192.168.0.152";
        int PortaUDP = 5683;
        
        DecimalFormat df = new DecimalFormat("0.00");
        
        byte funcao;
        byte EndReg;
        
        funcao = 4;
        
        if (funcao == 16) {
        	EndReg = 2;
        	EnvRecMsg.ModbusKron(IPConcArd, PortaUDP, funcao, EndReg, Verbose);
        }
        
        if ((funcao == 4) || (funcao == 3)) {
        	EndReg = 14;
        	EnvRecMsg.ModbusKron(IPConcArd, PortaUDP, funcao, EndReg, Verbose);
        	
        	EndReg = 16;
        	EnvRecMsg.ModbusKron(IPConcArd, PortaUDP, funcao, EndReg, Verbose);
        
        	EndReg = 22;
        	EnvRecMsg.ModbusKron(IPConcArd, PortaUDP, funcao, EndReg, Verbose);
        
        	EndReg = 28;
        	EnvRecMsg.ModbusKron(IPConcArd, PortaUDP, funcao, EndReg, Verbose);
        
        	EndReg = 34;
        	EnvRecMsg.ModbusKron(IPConcArd, PortaUDP, funcao, EndReg, Verbose);
        
        	EndReg = 40;
        	EnvRecMsg.ModbusKron(IPConcArd, PortaUDP, funcao, EndReg, Verbose);
        	
        	EndReg = 46;
        	EnvRecMsg.ModbusKron(IPConcArd, PortaUDP, funcao, EndReg, Verbose);
        
        	EndReg = 52;
        	EnvRecMsg.ModbusKron(IPConcArd, PortaUDP, funcao, EndReg, Verbose);
        
        	//funcao = 3;
        	//EndReg = 0;
        	//EnvRecMsg.BinUDP2(IPConcArd, PortaUDP, funcao, EndReg, Verbose);
        
        	//funcao = 3;
        	//EndReg = 2;
        	//EnvRecMsg.BinUDP2(IPConcArd, PortaUDP, funcao, EndReg, Verbose);
        }
        
        
        // 0x0080BB44
        //int F2;
        //int F1;
        //int F0;
        //int EXP;
                
        //F2 = 0x00; F1 = 0x80; F0 = 0xBB; EXP = 0x44;
        
        //double Valor = EnvRecMsg.ConverteIEEE754PfValor(F0, F1, F2, EXP);
        //Util.Terminal("Valor = " + df.format(Valor), false, Verbose);
        
        //F2 = 0x00; F1 = 0x00; F0 = 0x20; EXP = (0x03 + 0x7F) / 2;
        
        //Valor = EnvRecMsg.ConverteIEEE754PfValor(F0, F1, F2, EXP);
        //Util.Terminal("Valor = " + df.format(Valor) + " / F2 = " + F2 + " / F1 = " + F1 + " / F0 = " + F0 + " / EXP = " + EXP, false, Verbose);
            
	}

}
