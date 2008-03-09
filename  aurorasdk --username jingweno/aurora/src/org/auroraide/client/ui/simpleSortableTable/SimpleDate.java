package org.auroraide.client.ui.simpleSortableTable;


import java.util.Date;

public class SimpleDate extends Date {


	private static final long serialVersionUID = 1L;

	public SimpleDate(){
		super();
	}
	
	public SimpleDate(int year, int month, int date){
		super(year-1900, month-1, date);
	}
	
	
	
	public String toString(){
		return this.formatDate_DDMONYYYY(this);
	}

	/*
	 * formatDate_DDMONYYYY
	 * 
	 * Formats the date in DDMONYYYY format
	 * 
	 * @param (Date to be formatted)
	 * @return String
	 */
	private String formatDate_DDMONYYYY(Date date){
		String[] MONTHS = {
			"Jan",
			"Feb",
			"Mar",
			"Apr",
			"May",
			"Jun",
			"Jul",
			"Aug",
			"Sep",
			"Oct",
			"Nov",
			"Dec"
		};		
		StringBuffer dateStr = new StringBuffer();
		if(date.getDate() < 10){
			dateStr.append("0");
		}
		dateStr.append(date.getDate());
		dateStr.append(" ");
		
		dateStr.append(MONTHS[date.getDate()]);
		dateStr.append(" ");
		
		dateStr.append((date.getDate()+1900));
		return dateStr.toString();
	}			
	
	/*
	 * formatDate_MMDDYYYY
	 * 
	 * Formats the date in MMDDYYYY format
	 * 
	 * @param (Date to be formatted)
	 * @return String
	 */
	private String formatDate_MMDDYYYY (final Date date){
		StringBuffer strDate = new StringBuffer();
		if(this.getMonth() < 9){
			strDate.append("0");
		}
		strDate.append(this.getMonth()+1);
		strDate.append("/");
		if(this.getDate() < 10){
			strDate.append("0");
		}
		strDate.append(this.getDate());
		strDate.append("/");
		strDate.append(this.getDate()+1900);
		
		return strDate.toString();
	}
}
