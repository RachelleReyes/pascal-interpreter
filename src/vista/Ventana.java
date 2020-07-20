package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import controlador.ControladorBoton;
import javax.swing.SwingConstants;

public class Ventana extends JFrame {

	private JPanel contentPane;
	private JButton btnCargarArchivo;
	private JButton btnBorrarContenido;
	private JTextArea textAreaSource;
	private JButton btnVerResultado;
	private JComboBox comboBoxAnalizador;
	private JTextArea textAreaResult;
	private JButton btnGenerarArchivo;
	private JTextArea textAreaError;
	private JButton btnInterpretar; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ventana frame = new Ventana();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Ventana() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 839, 589);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelTexto = new JPanel();
		contentPane.add(panelTexto, BorderLayout.CENTER);
		GridLayout gl_panelTexto = new GridLayout();
		gl_panelTexto.setColumns(2);
		gl_panelTexto.setRows(0);
		panelTexto.setLayout(gl_panelTexto);
		
		textAreaSource = new JTextArea();
		panelTexto.add(textAreaSource);
		
		JScrollPane scrollSource = new JScrollPane(textAreaSource,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panelTexto.add(scrollSource, "name_15791785067400");
		
		JPanel panelResult = new JPanel();
		panelTexto.add(panelResult);
		panelResult.setLayout(new GridLayout(2, 1, 0, 0));
		
		textAreaResult = new JTextArea();
		panelTexto.add(textAreaResult);
		
		JScrollPane scrollResult = new JScrollPane(textAreaResult,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panelResult.add(scrollResult);
		
		JPanel panelError = new JPanel();
		panelResult.add(panelError);
		panelError.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("Error/Warning");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panelError.add(lblNewLabel, BorderLayout.NORTH);
		
		textAreaError = new JTextArea();
		panelError.add(textAreaError, BorderLayout.CENTER);
		
		JScrollPane scrollError = new JScrollPane(textAreaError,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panelError.add(scrollError);
		
		JPanel panelTitulos = new JPanel();
		contentPane.add(panelTitulos, BorderLayout.NORTH);
		panelTitulos.setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel panel1 = new JPanel();
		FlowLayout fl_panel1 = (FlowLayout) panel1.getLayout();
		panelTitulos.add(panel1);
		
		JLabel lblArchivoFuente = new JLabel("Archivo Fuente");
		lblArchivoFuente.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel1.add(lblArchivoFuente);
		
		JPanel panel2 = new JPanel();
		FlowLayout fl_panel2 = (FlowLayout) panel2.getLayout();
		panelTitulos.add(panel2);
		
		JLabel lblResultado = new JLabel("Resultado");
		lblResultado.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel2.add(lblResultado);
		
		JPanel panelBotones = new JPanel();
		contentPane.add(panelBotones, BorderLayout.SOUTH);
		panelBotones.setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel panelBotonFuente = new JPanel();
		panelBotones.add(panelBotonFuente);
		
		btnCargarArchivo = new JButton("Cargar Archivo");
		panelBotonFuente.add(btnCargarArchivo);
		
		btnBorrarContenido = new JButton("Borrar Contenido");
		panelBotonFuente.add(btnBorrarContenido);
		
		JPanel panelBotonResultados = new JPanel();
		panelBotones.add(panelBotonResultados);
		panelBotonResultados.setLayout(new GridLayout(2, 3, 0, 0));
		
		comboBoxAnalizador = new JComboBox();
		comboBoxAnalizador.setModel(new DefaultComboBoxModel(new String[] {"Analizador Lexico", "Analizador Sintactico", "Codigo Intermedio", "Tabla de Simbolos"}));
		panelBotonResultados.add(comboBoxAnalizador);
		
		btnVerResultado = new JButton("Ver Resultado");
		panelBotonResultados.add(btnVerResultado);
		
		btnInterpretar = new JButton("Interpretar");
		panelBotonResultados.add(btnInterpretar);
		
		btnGenerarArchivo = new JButton("Generar Archivo");
		panelBotonResultados.add(btnGenerarArchivo);
		
		ControladorBoton controlador=new ControladorBoton(this);
		btnCargarArchivo.setActionCommand("CARGAR_FUENTE");
		btnCargarArchivo.addActionListener(controlador);
		
		btnBorrarContenido.setActionCommand("BORRAR_FUENTE");
		btnBorrarContenido.addActionListener(controlador);
		
		btnVerResultado.setActionCommand("VER_RESULTADO");
		btnVerResultado.addActionListener(controlador);
		
		btnGenerarArchivo.setActionCommand("GENERAR_ARCHIVO");
		btnGenerarArchivo.addActionListener(controlador);
		
		btnInterpretar.setActionCommand("INTERPRETAR");
		btnInterpretar.addActionListener(controlador);
		
	}
	
	public void setTextAreaSource(String cadena) {
		textAreaSource.setText(cadena);
	}
	
	public String getTextAreaSource() {
		return textAreaSource.getText();
	}
	
	public String getEtapa() {
		String opcion;
		opcion = comboBoxAnalizador.getSelectedItem().toString();
		return opcion;
	}
	
	public void setTextAreaResult(String cadena) {
		textAreaResult.setText(cadena);
	}

	public void setTextAreaError(String cadena) {
		textAreaError.setText(cadena);
	}
}
