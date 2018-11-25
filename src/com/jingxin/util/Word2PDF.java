package com.jingxin.util;

import java.io.File;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;

public class Word2PDF {
		private static final int wdFormatPDF = 17;
		private static final int xlTypePDF = 0;
		private static final int ppSaveAsPDF = 32;
	 
		public static void main(String[] args) {
			Word2PDF word2pdf = new Word2PDF();
			word2pdf.convert2PDF("G:\\PDF-JS\\a.txt", "G:\\PDF-JS\\a.pdf");
		}
		public boolean convert2PDF(String inputFile, String pdfFile) {
			String suffix = getFileSufix(inputFile);
			File file = new File(inputFile);
			if (!file.exists()) {
				return false;
			}
			if (suffix.equals("pdf")) {
				return false;
			}
			if (suffix.equals("doc") || suffix.equals("docx")
					|| suffix.equals("txt")) {
				return word2PDF(inputFile, pdfFile);
			} else if (suffix.equals("ppt") || suffix.equals("pptx")) {
				return ppt2PDF(inputFile, pdfFile);
			} else if (suffix.equals("xls") || suffix.equals("xlsx")) {
				return excel2PDF(inputFile, pdfFile);
			} else {
				return false;
			}
		}
		public static String getFileSufix(String fileName) {
			int splitIndex = fileName.lastIndexOf(".");
			return fileName.substring(splitIndex + 1);
		}
		// wordת��Ϊpdf
		public static boolean word2PDF(String inputFile, String pdfFile) {
			try {
				// ��wordӦ�ó���
				ActiveXComponent app = new ActiveXComponent("Word.Application");
				// ����word���ɼ�
				app.setProperty("Visible", true);
				// ���word�����д򿪵��ĵ�,����Documents����
				Dispatch docs = app.getProperty("Documents").toDispatch();
				// ����Documents������Open�������ĵ��������ش򿪵��ĵ�����Document
				Dispatch doc = Dispatch.call(docs, "Open", inputFile, false, true)
						.toDispatch();
				// ����Document�����SaveAs���������ĵ�����Ϊpdf��ʽ
				/*
				 * Dispatch.call(doc, "SaveAs", pdfFile, wdFormatPDF
				 * //word����Ϊpdf��ʽ�ֵ꣬Ϊ17 );
				 */
				Dispatch.call(doc, "ExportAsFixedFormat", pdfFile, wdFormatPDF);// word����Ϊpdf��ʽ�ֵ꣬Ϊ17
				// �ر��ĵ�
				Dispatch.call(doc, "Close", false);
				// �ر�wordӦ�ó���
				app.invoke("Quit", 0);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		// excelת��Ϊpdf
		public static boolean excel2PDF(String inputFile, String pdfFile) {
			try {
				ActiveXComponent app = new ActiveXComponent("Excel.Application");
				app.setProperty("Visible", false);
				Dispatch excels = app.getProperty("Workbooks").toDispatch();
				Dispatch excel = Dispatch.call(excels, "Open", inputFile, false,
						true).toDispatch();
				Dispatch.call(excel, "ExportAsFixedFormat", xlTypePDF, pdfFile);
				Dispatch.call(excel, "Close", false);
				app.invoke("Quit");
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		// pptת��Ϊpdf
		public static boolean ppt2PDF(String inputFile, String pdfFile) {
			try {
				ActiveXComponent app = new ActiveXComponent(
						"PowerPoint.Application");
				// app.setProperty("Visible", msofalse);
				Dispatch ppts = app.getProperty("Presentations").toDispatch();
	 
				Dispatch ppt = Dispatch.call(ppts, "Open", inputFile, true,// ReadOnly
						true,// Untitledָ���ļ��Ƿ��б���
						false// WithWindowָ���ļ��Ƿ�ɼ�
						).toDispatch();
	 
				Dispatch.call(ppt, "SaveAs", pdfFile, ppSaveAsPDF);
	 
				Dispatch.call(ppt, "Close");
	 
				app.invoke("Quit");
				return true;
			} catch (Exception e) {
				return false;
			}
		}

}
