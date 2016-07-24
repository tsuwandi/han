package module.production.bl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import module.production.dao.GroupShiftDAO;
import module.production.dao.LineDAO;
import module.production.dao.MachineDAO;
import module.production.dao.ProdRMDAO;
import module.production.dao.ProductionDAO;
import module.production.dao.ProductionResultDAO;
import module.production.dao.ProductionResultDetailDAO;
import module.production.dao.ShiftDAO;
import module.production.model.GroupShift;
import module.production.model.Line;
import module.production.model.Machine;
import module.production.model.ProdRM;
import module.production.model.Production;
import module.production.model.ProductionResult;
import module.production.model.ProductionResultDetail;
import module.production.model.Shift;

public class ProductionBL {
	private DataSource dataSource;
	private LineDAO lineDAO;
	private ShiftDAO shiftDAO;
	private GroupShiftDAO groupShiftDAO;
	private MachineDAO machineDAO;
	private ProdRMDAO prodRMDAO;
	private ProductionDAO productionDAO;
	private ProductionResultDAO productionResultDAO;
	private ProductionResultDetailDAO productionResultDetailDAO;
	
	public ProductionBL(DataSource dataSource) {
		Connection con = null;
		this.dataSource = dataSource;
		try {
			con = dataSource.getConnection();
			shiftDAO = new ShiftDAO(con);
			lineDAO = new LineDAO(con);
			groupShiftDAO = new GroupShiftDAO(con);
			machineDAO = new MachineDAO(con);
			prodRMDAO = new ProdRMDAO(con);
			productionDAO = new ProductionDAO(con);
			productionResultDAO = new ProductionResultDAO(con);
			productionResultDetailDAO = new ProductionResultDetailDAO(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Line> getLine() throws SQLException {
		return lineDAO.getAll();
	}
	
	public List<Machine> getMachine() throws SQLException {
		return machineDAO.getAll();
	}
	
	public List<Shift> getShift() throws SQLException {
		return shiftDAO.getAll();
	}
	public List<GroupShift> getGroupShift() throws SQLException {
		return groupShiftDAO.getAll();
	}
	
	public String getProductionLastCode() throws SQLException{
		String lastCode = productionDAO.getLastCode();
		if(lastCode==null){
			lastCode = ("0001");
		}else{
			String [] splittedCode = lastCode.split("/");
			int tempIntCode = Integer.valueOf(splittedCode[0])+1;
			String textTemp = String.valueOf(tempIntCode);
			if(textTemp.length()==1){
				lastCode ="000"+textTemp;
			}else if(textTemp.length()==2){
				lastCode="00"+textTemp;
			}else if(textTemp.length()==3){
				lastCode="0"+textTemp;
			}else{
				lastCode=textTemp;
			}
		}
		return lastCode;
	}
	
	public String getProductionResultLastCode() throws SQLException{
		String lastCode = productionResultDAO.getLastCode();
		if(lastCode==null){
			lastCode = ("0001");
		}else{
			String [] splittedCode = lastCode.split("/");
			int tempIntCode = Integer.valueOf(splittedCode[0])+1;
			String textTemp = String.valueOf(tempIntCode);
			if(textTemp.length()==1){
				lastCode ="000"+textTemp;
			}else if(textTemp.length()==2){
				lastCode="00"+textTemp;
			}else if(textTemp.length()==3){
				lastCode="0"+textTemp;
			}else{
				lastCode=textTemp;
			}
		}
		return lastCode;
	}
	
	public List<Production> getProduction() throws SQLException {
		List<Production> productions = productionDAO.getAll();
		for (Production production : productions) {
			production.setProductionResult(getProductionResultByCode(production.getProductionCode()));
			production.setListOfProdRM(getProductRMByCode(production.getProductionCode()));
		}
		return productionDAO.getAll();
	}
	
	private ProductionResult getProductionResultByCode(String productionCode) throws SQLException {
		ProductionResult productionResult = productionResultDAO.getAllByProductionCode(productionCode);
		productionResult.setListOfProductionResultDetail(getProductionResultDetailByCode(productionResult.getProdResultCode()));
		return productionResult;
	}
	private List<ProductionResultDetail> getProductionResultDetailByCode(String prodResultCode) throws SQLException {
		return productionResultDetailDAO.getAllByProdResultCode(prodResultCode);
	}
	
	private List<ProdRM> getProductRMByCode(String productionCode)throws SQLException{
		return prodRMDAO.getAllByProductionCode(productionCode);
	}
	
	public List<ProdRM> getSearchProdRM()throws SQLException{
		return prodRMDAO.getAllSearch();				
	}
	
	public ProdRM getSearchProdRMByPalletCard(String palletCardCode)throws SQLException{
		return prodRMDAO.getProdRMByPalletCard(palletCardCode);				
	}
	
	public void saveAll(Production production)throws SQLException {
		Connection con = null;
		boolean flagProductionResult=false;
		boolean flagProductionRawMaterial=false;
		try {
			con = dataSource.getConnection();
			con.setAutoCommit(false);
			if(production.getProductionResult()!=null){
				new ProductionResultDAO(con).save(production.getProductionResult());
				for(ProductionResultDetail prd : production.getProductionResult().getListOfProductionResultDetail()){
					new ProductionResultDetailDAO(con).save(prd);
				}
				flagProductionResult=true;
			}
			if(production.getListOfProdRM()!=null||production.getListOfProdRM().size()!=0){
				for(ProdRM prodRM :production.getListOfProdRM()){
					new ProdRMDAO(con).save(prodRM);
				}
				flagProductionRawMaterial=true;
			}
			if(flagProductionRawMaterial&&flagProductionResult)production.setStatus("Complete");
			else production.setStatus("InComplete");
			new ProductionDAO(con).save(production);
			con.commit();
		} catch (Exception e) {
			con.rollback();
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		}finally {
			con.close();
		}
	}
	
	public void updateAll(Production production)throws SQLException {
		Connection con = null;
		boolean flagProductionResult=false;
		boolean flagProductionRawMaterial=false;
		try {
			con = dataSource.getConnection();
			con.setAutoCommit(false);
			if(production.getProductionResult()!=null){
				new ProductionResultDAO(con).update(production.getProductionResult());
				new ProductionResultDetailDAO(con).delete(production.getProductionResult().getProdResultCode());
				for(ProductionResultDetail prd : production.getProductionResult().getListOfProductionResultDetail()){
					new ProductionResultDetailDAO(con).save(prd);
				}
				flagProductionResult=true;
			}
			if(production.getListOfProdRM()!=null||production.getListOfProdRM().size()!=0){
				new ProdRMDAO(con).delete(production.getProductionCode());
				for(ProdRM prodRM :production.getListOfProdRM()){
					new ProdRMDAO(con).save(prodRM);
				}
				flagProductionRawMaterial=true;
			}
			if(flagProductionRawMaterial&&flagProductionResult)production.setStatus("Complete");
			else production.setStatus("InComplete");
			new ProductionDAO(con).update(production);
			con.commit();
		} catch (Exception e) {
			con.rollback();
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		}finally {
			con.close();
		}
	}
}