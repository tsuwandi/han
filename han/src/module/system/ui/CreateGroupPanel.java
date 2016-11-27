package module.system.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import module.system.model.Group;
import controller.ServiceFactory;

public class CreateGroupPanel extends JPanel{

	private static final long serialVersionUID = 2513611527485742789L;
	private JTextField groupNameField;
	private JTextArea groupDescField;

	public CreateGroupPanel() {
		setSize(1024, 630);
		setLayout(null);
		
		JLabel breadCrumbLbl = new JLabel("Konfigurasi > Sistem > Group");
		breadCrumbLbl.setFont(new Font("Tahoma", Font.BOLD, 12));
		breadCrumbLbl.setBounds(50, 10, 214, 25);
		add(breadCrumbLbl);
		
		JLabel lblHeader = new JLabel("BUAT GROUP BARU");
		lblHeader.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblHeader.setBounds(50, 46, 150, 25);
		add(lblHeader);
		
		JLabel label = new JLabel("<html>Nama Group<font color='red'> * </font></html>");
		label.setBounds(30, 80, 100, 30);
		add(label);
		
		JLabel label_1 = new JLabel(":");
		label_1.setBounds(130, 80, 10, 30);
		add(label_1);
		
		JLabel label_2 = new JLabel("Deskripsi Group");
		label_2.setBounds(30, 120, 100, 30);
		add(label_2);
		
		JLabel label_3 = new JLabel(":");
		label_3.setBounds(130, 120, 10, 30);
		add(label_3);
		
		groupDescField = new JTextArea();
		groupDescField.setEnabled(false);
		groupDescField.setEditable(false);
		groupDescField.setBounds(140, 120, 200, 90);
		add(groupDescField);
		
		groupNameField = new JTextField();
		groupNameField.setEnabled(false);
		groupNameField.setEditable(false);
		groupNameField.setColumns(10);
		groupNameField.setBounds(140, 80, 200, 30);
		add(groupNameField);
		
		JButton saveBtn = new JButton("Simpan");
		saveBtn.setBounds(924, 589, 90, 30);
		add(saveBtn);
		
		saveBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
	}

	protected void save() {
		
		Group group = new Group();
		
		if (groupNameField.getText()==null) {
			group.setGroupName(groupNameField.getText());
		} else {
			// muncul pop up
		}
		group.setGroupDesc(groupDescField.getText());
		
		ServiceFactory.getSystemBL().saveGroup(group);
	}
}