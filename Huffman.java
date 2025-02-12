package Huffman;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
class EDecodificacion extends Exception{
	public EDecodificacion() {
		super("Error al decodificar");
	}
}
class MensajesCodificados implements Serializable {
	
	private static final long serialVersionUID = 3050052724387243053L;
	private ArrayList<Character> caracteres;
	private ArrayList<String> codigosCaracteres;
	private String mensajeCodificado;
	private NodoB raices;
	private String id;
	

	public MensajesCodificados(ArrayList<Character> caracteres, ArrayList<String> codigosCaracteres,
			String mensajeCodificado, NodoB raices,String id) {
		super();
		this.id=id;
		this.caracteres = caracteres;
		this.codigosCaracteres = codigosCaracteres;
		this.mensajeCodificado = mensajeCodificado;
		this.raices = raices;
	}
	
	

	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public ArrayList<Character> getCaracteres() {
		return caracteres;
	}

	public void setCaracteres(ArrayList<Character> caracteres) {
		this.caracteres = caracteres;
	}

	public ArrayList<String> getCodigosCaracteres() {
		return codigosCaracteres;
	}

	public void setCodigosCaracteres(ArrayList<String> codigosCaracteres) {
		this.codigosCaracteres = codigosCaracteres;
	}

	public String getMensajeCodificado() {
		return mensajeCodificado;
	}

	public void setMensajesCodificados(String mensajeCodificado) {
		this.mensajeCodificado = mensajeCodificado;
	}

	public NodoB getRaices() {
		return raices;
	}

	public void setRaices(NodoB raices) {
		this.raices = raices;
	}
	

}

class Ficheros {
	private String ficheroMensajesCodificados;

	public Ficheros() {
		String dir = System.getProperty("user.dir");
		ficheroMensajesCodificados = dir + File.separator + "Ficheros" + File.separator + "MensajesCodificados";

		File mensajesDir = new File(ficheroMensajesCodificados);
		if (!mensajesDir.exists())
			mensajesDir.mkdirs();
	}
	
	
	public String readTexto(String file) throws IOException, ClassNotFoundException {
	FileReader r= new FileReader(file);
	
	BufferedReader reader= new BufferedReader(r);
	String resultado ="";
	String texto;
	while((texto = reader.readLine())!= null) {
		resultado = resultado + texto;
		}
	reader.close();
	return resultado;
	}
	
	public void escribirTexto(String mensajeCodificado, String textoFichero) throws IOException {
		BufferedWriter writer= new BufferedWriter(new FileWriter("Huffman"+textoFichero));
		writer.write(mensajeCodificado);
		writer.close();
		
	}
	
	public void escribirTextoAUXILIAR(String mensajeCodificado,String file) throws IOException {
		BufferedWriter writer= new BufferedWriter(new FileWriter("ASCII"+file));
		writer.write(mensajeCodificado);
		writer.close();
		
	}

	public void escribirMensajesCodificados(LinkedList<MensajesCodificados> p) throws IOException {
		int len = p.size();
		for (int i = 0; i < len; i++) {
			FileOutputStream f = new FileOutputStream(ficheroMensajesCodificados + File.separator
					+ "MensajeCodificados " + p.get(i).getId() + ".mensajecodificados");
			ObjectOutputStream obj = new ObjectOutputStream(f);

			obj.writeObject(p.get(i));
			obj.close();
			f.close();
		}
	}

	public LinkedList<MensajesCodificados> readMensajesCodificados() throws IOException, ClassNotFoundException {
		File f = new File(ficheroMensajesCodificados);
		File[] ficherosMensajesCodificados = f
				.listFiles((dir, nombre) -> nombre.toLowerCase().endsWith(".mensajecodificados"));
		LinkedList<MensajesCodificados> mensajesCodificados = new LinkedList<MensajesCodificados>();
		
			int len = ficherosMensajesCodificados.length;
			for (int i = 0; i < len; i++) {
				FileInputStream in = new FileInputStream(ficherosMensajesCodificados[i]);
				ObjectInputStream o = new ObjectInputStream(in);
				mensajesCodificados.add((MensajesCodificados) o.readObject());
				o.close();
				in.close();
			}		
		return mensajesCodificados;
	}

	public void eliminarMensajesCodificados(String id) {
		File file = new File(ficheroMensajesCodificados + File.separator + "MensajeCodificados " + id + ".mensajecodificados");
		String dir = System.getProperty("user.dir");
		if (file.exists()) {
			file.delete();
		}
		File fileCodificacionH= new File(dir + File.separator+ "Huffman" + id);
		if(fileCodificacionH.exists()) {
			fileCodificacionH.delete();
		}
		
		File fileCodificacionA= new File(dir + File.separator+ "ASCII" + id);
		if(fileCodificacionA.exists()) {
			fileCodificacionA.delete();
		}
		
		File fileCodificacion= new File(dir + File.separator+ id);
		if(fileCodificacion.exists()) {
			fileCodificacion.delete();
		}
	}

}

public class Huffman {

	private Ficheros ficheros;
	private LinkedList<MensajesCodificados> mensajesCodificados;

	public Ficheros getFicheros() {
		return ficheros;
	}

	public void setFicheros(Ficheros ficheros) {
		this.ficheros = ficheros;
	}

	public LinkedList<MensajesCodificados> getMensajesCodificados() {
		return mensajesCodificados;
	}

	public void setMensajesCodificados(LinkedList<MensajesCodificados> mensajesCodificados) {
		this.mensajesCodificados = mensajesCodificados;
	}

	public Huffman(LinkedList<MensajesCodificados> mensajesCodificados) throws ClassNotFoundException, IOException {
		super();
		this.mensajesCodificados = mensajesCodificados;
		ficheros = new Ficheros();
		
		cargarMensajesCodificados();
	}

	private void cargarMensajesCodificados() throws ClassNotFoundException, IOException {
		
			LinkedList<MensajesCodificados> mensajesLeidos = ficheros.readMensajesCodificados();
			mensajesCodificados.addAll(mensajesLeidos);
		
	}

	public void guardarFicheros() throws IOException {
		ficheros.escribirMensajesCodificados(mensajesCodificados);
	}

	public void conseguirFrecuencias(String texto, ArrayList<Character> caracteresUnicos,
			ArrayList<Integer> frecuencias) {
		for (char c : texto.toCharArray()) {
			int index = caracteresUnicos.indexOf(c);
			if (index == -1) {
				caracteresUnicos.add(c);
				frecuencias.add(1);
			} else {
				frecuencias.set(index, frecuencias.get(index) + 1);
			}
		}
	}
	
	public String convertirStringABinario(String texto) {
		String resultado = "";
		for(char c: texto.toCharArray()) {
			resultado = resultado + "0" + Integer.toBinaryString(c);
		}
		return resultado;
	}

	public String codificar(String texto,String textoFichero) throws IOException {
		MensajesCodificados objeto;
		NodoB raiz;
		ArrayList<Character> caracteresUnicos = new ArrayList<Character>();
		ArrayList<Integer> frecuencias = new ArrayList<Integer>();
		ArrayList<Character> caracteresNuevos = new ArrayList<Character>();
		ArrayList<String> codigos = new ArrayList<String>();
		conseguirFrecuencias(texto, caracteresUnicos, frecuencias);
		Queue<NodoB> cola = new PriorityQueue<>();
		for (int i = 0; i < frecuencias.size(); i++) {
			cola.add(new Hoja(frecuencias.get(i), caracteresUnicos.get(i)));
		}
		if (cola.size() == 1) {
			NodoB unicoNodo = cola.poll();
			raiz = unicoNodo;
			caracteresNuevos.add(((Hoja) raiz).getCharacter());
			codigos.add("0");
		} else {
			while (cola.size() > 1) {
				cola.add(new NodoB(cola.poll(), cola.poll()));
			}

			raiz = cola.poll();

			generarCodigos(raiz, "", caracteresNuevos, codigos);
		}

		String mensaje = textoCodificado(texto, caracteresNuevos, codigos);
	
        ficheros.escribirTexto(mensaje,textoFichero);
		objeto = new MensajesCodificados(caracteresNuevos, codigos, mensaje, raiz,textoFichero);
		mensajesCodificados.add(objeto);
		return mensaje;
	}

	private void generarCodigos(NodoB nodo, String codigo, ArrayList<Character> caracteresNuevos,
			ArrayList<String> codigos) {
		if (nodo instanceof Hoja) {
			caracteresNuevos.add(((Hoja) nodo).getCharacter());
			codigos.add(codigo);
			return;
		}

		generarCodigos(nodo.getHijoIzq(), codigo.concat("0"), caracteresNuevos, codigos);
		generarCodigos(nodo.getHijoDer(), codigo.concat("1"), caracteresNuevos, codigos);
	}

	private String textoCodificado(String texto, List<Character> letras, List<String> codigos) {
		String resultado = "";
		for (char c : texto.toCharArray()) {
			int index = letras.indexOf(c);
			resultado += codigos.get(index);
		}

		return resultado;
	}

	public String decode(NodoB cabezaArbol, String codigo) throws EDecodificacion{
		try {
		return decode(cabezaArbol, codigo, 0, cabezaArbol, "");}
		catch(StackOverflowError e) {
			throw new EDecodificacion();
		}
		
	}

	private String decode(NodoB actual, String codigo, int indiceActual, NodoB cabezaArbol, String resultado) {
		if (indiceActual >= codigo.length() && actual.equals(cabezaArbol))
			return resultado;
		if (actual instanceof Hoja) {
			Hoja n = (Hoja) actual;
			resultado += Character.toString(n.getCharacter());
			if (actual.equals(cabezaArbol)) {
				return decode(cabezaArbol, codigo, indiceActual + 1, cabezaArbol, resultado);
			}
			return decode(cabezaArbol, codigo, indiceActual, cabezaArbol, resultado);
		}
		if (codigo.charAt(indiceActual) == '0')
			return decode(actual.getHijoIzq(), codigo, ++indiceActual, cabezaArbol, resultado);

		return decode(actual.getHijoDer(), codigo, ++indiceActual, cabezaArbol, resultado);
	}

	public void eliminar(String id) {
		int i = 0;
	    while (i < mensajesCodificados.size() 
	            && !mensajesCodificados.get(i).getId().equals(id)) {
	        i++;
	    }
			
		
		ficheros.eliminarMensajesCodificados(mensajesCodificados.get(i).getId());
		mensajesCodificados.remove(i);
	}
	 private String generarIdentificadorUnico() {
	        Random random = new Random();
	        int numero = random.nextInt(999999);

	        String nuevoIdentificador = String.format("MSG-%06d", numero);
	        while (!esIdentificadorUnico(nuevoIdentificador)) {
	            numero = random.nextInt(999999);
	            nuevoIdentificador = String.format("MSG-%06d", numero);
	        }

	        return nuevoIdentificador;
	    }

	    private boolean esIdentificadorUnico(String identificador) {
	    	int i=0;
	    	boolean bandera=true;
	    	while(i<mensajesCodificados.size()&&bandera) {
	    		if (mensajesCodificados.get(i).getId().equals(identificador)) {
	                bandera=false;
	            }
	    		i++;
	    	}
	       
	        return bandera;
	    }

}

class Hoja extends NodoB implements Serializable {
	
	private static final long serialVersionUID = -6403457415799744452L;
	private char Character;

	public Hoja(int llave, char Character) {
		super(llave);
		this.Character = Character;

	}

	public char getCharacter() {
		return Character;
	}

	public void setCharacter(char character) {
		Character = character;
	}

}

class NodoB implements Comparable<NodoB>, Serializable {
	
	private static final long serialVersionUID = -8739620177293876810L;
	protected int freq;
	protected NodoB hijoIzq;
	protected NodoB hijoDer;

	protected NodoB padre;

	public NodoB(NodoB hijoIzq, NodoB hijoDer) {
		super();
		this.freq = hijoIzq.getLlave() + hijoDer.getLlave();
		this.hijoIzq = hijoIzq;
		this.hijoDer = hijoDer;
		
	}

	public NodoB(int llave) {
		super();
		this.freq = llave;
	}

	public int getLlave() {
		return freq;
	}

	public void setLlave(int llave) {
		this.freq = llave;
	}

	public NodoB getHijoIzq() {
		return hijoIzq;
	}

	public void setHijoIzq(NodoB hijoIzq) {
		if (hijoIzq != null) {
			hijoIzq.setPadre(this);
		}
		this.hijoIzq = hijoIzq;
	}

	public NodoB getHijoDer() {
		return hijoDer;
	}

	public void setHijoDer(NodoB hijoDer) {
		if (hijoDer != null) {
			hijoDer.setPadre(this);
		}
		this.hijoDer = hijoDer;
	}

	public NodoB getPadre() {
		return padre;
	}

	public void setPadre(NodoB padre) {
		this.padre = padre;
	}

	@Override
	public int compareTo(NodoB o1) {
		return this.freq - o1.getLlave();
	}
}