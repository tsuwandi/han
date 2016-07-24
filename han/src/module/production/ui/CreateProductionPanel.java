package module.production.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

import controller.ServiceFactory;
import main.component.ComboBox;
import module.production.model.GroupShift;
import module.production.model.Line;
import module.production.model.Shift;
import module.util.Bridging;
import module.production.model.Production;

public class CreateProductionPanel extends JPanel implements Bridging{
	private static final long serialVersionUID = 1L;
	
	private JLabel productionCodeLbl;
	private JLabel productionDateLbl;
	private JLabel groupShiftLbl;
	private JLabel shiftLbl;
	private JLabel lineLbl;
	
	private JLabel errorGroupShiftLbl;
	private JLabel errorLineLbl;
	private JLabel errorShiftLbl;
	
	private JTextField productionCodeField;
	private JDateChooser productionDateChooser;
	private ComboBox<GroupShift> groupShiftCmb;
	private ComboBox<Shift> shiftCmb;
	private ComboBox<Line> lineCmb;
	
	private JButton inputMaterialBtn;
	private JButton inputProductionResultBtn;
	private JButton saveBtn;
	
	private Production production;
	private CreateProductionPanel parent;
	
	public CreateProductionPanel(){
		parent = this;
		production = new Production();
		createGUI();
		listener();
		initData();
	}
	
	private void listener(){
		inputMaterialBtn.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				PopUpInputMaterial pop = new PopUpInputMaterial(parent);
				pop.show();
				pop.setLocationRelativeTo(null);
			}
		});
		
		inputProductionResultBtn.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				PopUpProductionResult pop = new PopUpProductionResult(parent);
				pop.show();
				pop.setLocationRelativeTo(null);
			}
		});
		
		saveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
	}
	
	private void initData(){
		List<Shift> shifts = new ArrayList<>();
		List<Line> lines = new ArrayList<>();
		List<GroupShift> groupShifts = new ArrayList<>();
		
		productionCodeField.setEnabled(false);
		try {
			lines = ServiceFactory.getProductionBL().getLine();
			shifts = ServiceFactory.getProductionBL().getShift();
			groupShifts = ServiceFactory.getProductionBL().getGroupShift();
			
			lines.add(0,new Line("--Pilih--"));
			shifts.add(0,new Shift("--Pilih--"));
			groupShifts.add(0,new GroupShift("--Pilih--"));
			
			lineCmb.setList(lines);
			shiftCmb.setList(shifts);
			groupShiftCmb.setList(groupShifts);
			Date currentDate = new Date();
			String date = new SimpleDateFormat("dd").format(currentDate);
			String month = new SimpleDateFormat("MM").format(currentDate);
			String year = new SimpleDateFormat("yy").format(currentDate);
			productionCodeField.setText(ServiceFactory.getProductionBL().getProductionLastCode()+"/PD/"+date+"/"+month+"/"+year);
			productionDateChooser.setDate(currentDate);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void createGUI(){
		setLayout(null);
		
		//TODO Title Area
		JLabel lblBreadcrumb = new JLabel("ERP > Produksi");
		lblBreadcrumb.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblBreadcrumb.setBounds(50, 10, 320, 30);
		add(lblBreadcrumb);

		JLabel lblHeader = new JLabel("INPUT PRODUKSI");
		lblHeader.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblHeader.setBounds(50, 45, 320, 30);
		add(lblHeader);
		
		//TODO ProductionCode Area
		productionCodeLbl = new JLabel("Kode Produksi");
		productionCodeLbl.setBounds(30,120,150,20);
		add(productionCodeLbl);
		
		productionCodeField = new JTextField();
		productionCodeField.setBounds(190, 120, 150, 20);
		add(productionCodeField);
		
		//TODO ProductionDate Area
		productionDateLbl = new JLabel("Tanggal Produksi");
		productionDateLbl.setBounds(30,160,150,20);
		add(productionDateLbl);
		
		productionDateChooser = new JDateChooser();
		productionDateChooser.setBounds(190,160,150,20);
		add(productionDateChooser);
		
		//TODO GroupShift Area
		groupShiftLbl = new JLabel("Group Shift");
		groupShiftLbl.setBounds(30,200,150,20);
		add(groupShiftLbl);
		
		groupShiftCmb = new ComboBox<>();
		groupShiftCmb.setBounds(190,200,150,20);
		add(groupShiftCmb);
		
		errorGroupShiftLbl = new JLabel();
		errorGroupShiftLbl.setBounds(345,200,150,20);
		add(errorGroupShiftLbl);
		
		//TODO Shift Area
		shiftLbl = new JLabel("Shift");
		shiftLbl.setBounds(30,240,150,20);
		add(shiftLbl);
		
		shiftCmb = new ComboBox<>();
		shiftCmb.setBounds(190,240,150,20);
		add(shiftCmb);
		
		errorShiftLbl = new JLabel();
		errorShiftLbl.setBounds(345,240,150,20);
		add(errorShiftLbl);
		
		//TODO Line Area
		lineLbl = new JLabel("Line");
		lineLbl.setBounds(30,280,150,20);
		add(lineLbl);
		
		lineCmb = new ComboBox<>();
		lineCmb.setBounds(190,280,150,20);
		add(lineCmb);
		
		errorLineLbl = new JLabel();
		errorLineLbl.setBounds(345,280,150,20);
		add(errorLineLbl);
		
		//TODO Button Area
		inputMaterialBtn = new JButton("Input Bahan Baku");
		inputMaterialBtn.setBounds(280,360,150,50);
		add(inputMaterialBtn);
		
		inputProductionResultBtn = new JButton("Input Hasil Produksi");
		inputProductionResultBtn.setBounds(450,360,150,50);
		add(inputProductionResultBtn);
		
		saveBtn = new JButton("Save");
		saveBtn.setBounds(610,360,150,50);
		add(saveBtn);
	}
	
	private void save(){
		int error = 0;
		if(groupShiftCmb.getSelectedIndex()==0){
			errorGroupShiftLbl.setText("<html><font color='red'>Group Shift harus dipilih !</font></html>");
			error++;
		}else{
			errorGroupShiftLbl.setText("");
		}
		
		if(shiftCmb.getSelectedIndex()==0){
			errorShiftLbl.setText("<html><font color='red'>Shift harus dipilih !</font></html>");
			error++;
		}else{
			errorShiftLbl.setText("");
		}
		
		if(lineCmb.getSelectedIndex()==0){
			errorLineLbl.setText("<html><font color='red'>Line harus dipilih !</font></html>");
			error++;
		}else{
			errorLineLbl.setText("");
		}
	}

	@Override
	public void invokeObjects(Object... objects) {
		if(objects.length!=0)production = (Production)objects[0];
		if(production!=null){
			productionCodeField.setText(production.getProductionCode());
			productionDateChooser.setDate(production.getProductionDate());
			groupShiftCmb.setSelectedItem(production.getGroupShiftDescription());
			shiftCmb.setSelectedItem(production.getShiftName());
			lineCmb.setSelectedItem(production.getLineDescription());
		}
	}

	public Production getProduction() {
		return production;
	}

	public void setProduction(Production production) {
		this.production = production;
	}
	
	
	
}
