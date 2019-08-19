package Models;

import Common.DriverType;

public class CommonConfig {
	public String TestName; 
    public String Version ;
    public String OutputTestFile ;
    public String ReportPath ;
    public String Type ;
    
  public DriverType getDriverType() {
	  return (DriverType)Enum.valueOf(DriverType.class, Type);
  }
}
