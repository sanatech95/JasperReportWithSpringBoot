package com.jasper.springboot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jasper.springboot.model.Student;
import com.jasper.springboot.model.Subject;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@RestController
@RequestMapping("/api/student")
public class StudentController {
	
	@GetMapping("/report")
	public ResponseEntity<byte[]> getReport() {

		try {
			String filePath = ResourceUtils.getFile("classpath:Student.jrxml")
					.getAbsolutePath();
			
			Subject subject1 = new Subject("Java", 80);
			Subject subject2 = new Subject("MySQL", 70);
			Subject subject3 = new Subject("PHP", 50);
			Subject subject4 = new Subject("MongoDB", 40);
			Subject subject5 = new Subject("C++", 60);
			
			List<Subject> list = new ArrayList<Subject>();
			list.add(subject1);
			list.add(subject2);
			list.add(subject3);
			list.add(subject4);
			list.add(subject5);
			
			JRBeanCollectionDataSource dataSource = 
					new JRBeanCollectionDataSource(list);
			
			JRBeanCollectionDataSource chartDataSource = 
					new JRBeanCollectionDataSource(list);
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("studentName", "John");
			parameters.put("tableData", dataSource);
			parameters.put("subReport", getSubReport());
			parameters.put("subDataSource", getSubDataSource());
			parameters.put("subParameters", getSubParameters());
			
			JasperReport report = JasperCompileManager.compileReport(filePath);
			
			JasperPrint print = 
					JasperFillManager.fillReport(report, parameters, chartDataSource);
			
			byte[] byteArray = JasperExportManager.exportReportToPdf(print);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("filename", "student.pdf");
			
			return new ResponseEntity<byte[]>(byteArray, headers, HttpStatus.OK);
			
		} catch(Exception e) {
			System.out.println("Exception while creating report");
			return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	public static JasperReport getSubReport () {
		
		JasperReport report;
		try {
			String filePath = ResourceUtils.getFile("classpath:FirstReport.jrxml").getAbsolutePath();
			report = JasperCompileManager.compileReport(filePath);
			return report;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static JRBeanCollectionDataSource getSubDataSource () {
		Student student1 = new Student(1L, "Raj", "Joshi", "Happy Street", "Delhi");
		
		Student student2 = new Student(2L, "Peter", "Smith", "Any Street", "Mumbai");
		
		List<Student> list = new ArrayList<Student>();
		list.add(student1);
		list.add(student2);
		
		JRBeanCollectionDataSource dataSource = 
				new JRBeanCollectionDataSource(list);
		
		return dataSource;
	}
	
	public static Map<String, Object> getSubParameters () {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("studentName", "Raj");
		
		return parameters;
	}
	
}
