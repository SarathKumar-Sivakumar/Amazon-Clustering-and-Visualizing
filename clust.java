import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.clusterers.SimpleKMeans;

import java.io.BufferedReader;
import java.io.FileReader;

@WebServlet("/clust")
public class clust extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public clust() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter outs=response.getWriter();
		
		HttpSession sessions=request.getSession(false);
		String clusts = null;
		String fileNames = null;
		try {
		
			List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
			for (FileItem item : items) {
				if (item.isFormField()) {
		
					String fieldName = item.getFieldName();
					String fieldValue = item.getString();
					if(fieldName.equals("cluster"))
					{
						clusts =fieldValue;
					}

				} else {
					String fieldName = item.getFieldName();
					fileNames = FilenameUtils.getName(item.getName());
					InputStream fileContent = item.getInputStream();
					File targetFile = new File(fileNames);
					FileUtils.copyInputStreamToFile(fileContent,targetFile);
				}}
		
			// csv to arff 
			CSVLoader loader = new CSVLoader();
			loader.setSource(new File(fileNames));
			Instances data = loader.getDataSet();

			String newfile[] = fileNames.split("\\.");
			String out_file = newfile[0] + ".arff";

			ArffSaver saver = new ArffSaver();
			saver.setInstances(data);
			saver.setFile(new File(out_file));
			saver.setDestination(new File(out_file));
			saver.writeBatch();

			//K-means
			SimpleKMeans kmeans = new SimpleKMeans();
			kmeans.setSeed(5);
			kmeans.setPreserveInstancesOrder(true);
			kmeans.setNumClusters(Integer.parseInt(clusts));

			BufferedReader inp_data= new BufferedReader(new FileReader(out_file));

			Instances clustdata = new Instances(inp_data);
			kmeans.buildClusterer(clustdata);

			int[] assignments = kmeans.getAssignments();
			JsonArray clusterArray=new JsonArray();

			ArrayList<Integer> Prcp = new ArrayList();
			ArrayList<Integer> Wind = new ArrayList();

			int j=0;
			for(j=0;j<clustdata.numInstances();j++){
				Prcp.add((int) clustdata.instance(j).value(2));
				Wind.add((int) clustdata.instance(j).value(3));
			}
			int i=0;
			
			//Json object
			for(int clusterNumber : assignments) {
				outs.write("5");
				outs.write("Instance"+ i+"-->"+ clusterNumber);
				JsonObject clusterObj=new JsonObject();
				JsonObject outData=new JsonObject();
				outData.addProperty("Prcp", Prcp.get(i));
				outData.addProperty("Wind", Wind.get(i));
				outData.addProperty("cluster", clusterNumber);
				clusterArray.add(outData);

				i++;
			}
			System.out.println(clusterArray);
			request.setAttribute("out_data", clusterArray);
			RequestDispatcher rd = request.getRequestDispatcher("display.jsp");
			rd.forward(request,response);

		}

		catch (Exception e) {
			e.printStackTrace();
			outs.write(e.toString());
		
		}

	}

}
