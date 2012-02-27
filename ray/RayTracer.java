package ray;

import java.io.File;
import java.util.ArrayList;

import ray.light.Light;
import ray.math.Color;
import ray.math.Vector3;

/**
 * A simple ray tracer.
 *
 * @author ags, modified by DS 2/2012
 */
public class RayTracer {

	public static String testFolderPath;
	
	public static String getTestFolderPath() { return testFolderPath; }
	/**
	 * If filename is a directory, set testFolderPath = fn.
	 * And return a list of all .xml files inside the directory
	 * @param fn Filename or directory
	 * @return fn itself in case fn is a file, or all .xml files inside fn
	 */
	public static final ArrayList<String> getFileLists(String fn) {
		if(fn.endsWith("/"))
			fn.substring(0, fn.length()-1);
		
		File file = new File(fn);
		ArrayList<String> output = new ArrayList<String>();
		if(file.exists()) {
			if(file.isFile()) {
				if(file.getParent() != null)
					testFolderPath = file.getParent() + "/";
				else
					testFolderPath = "";
				output.add(fn);
			} else {
				testFolderPath = fn + "/";
				for(String fl : file.list()) {
					if(fl.endsWith(".xml")) {
						output.add(testFolderPath + fl);
					}
				}	
			}
		} else
			System.out.println("File not found.");

		return output;
	}
	/**
	 * The main method takes all the parameters an assumes they are input files
	 * for the ray tracer. It tries to render each one and write it out to a PNG
	 * file named <input_file>.png.
	 *
	 * @param args
	 */
	public static final void main(String[] args) {

		Parser parser = new Parser();
		for (int ctr = 0; ctr < args.length; ctr++) {

			ArrayList<String> fileLists = getFileLists(args[ctr]);
			
			for (String inputFilename : fileLists) {
				String outputFilename = inputFilename + ".png";
	
				// Parse the input file
				Scene scene = (Scene) parser.parse(inputFilename, Scene.class);
				System.out.printf("Rendering %-25s  ", inputFilename);
				
				// Render the scene
				renderImage(scene);
	
				// Write the image out
				scene.getImage().write(outputFilename);
			}
		}
	}

	/**
	 * The renderImage method renders the entire scene.
	 *
	 * @param scene The scene to be rendered
	 */
	public static void renderImage(Scene scene) {

		// Get the output image
		Image image = scene.getImage();
		Camera cam = scene.getCamera();

		// Set the camera aspect ratio to match output image
		int width = image.getWidth();
		int height = image.getHeight();

		// Timing counters
		long startTime = System.currentTimeMillis();

		Ray ray = new Ray();
		Color pixelColor = new Color();
		Color rayColor = new Color();

		int total = height * width;
		int counter = 0;
		int lastShownProgress = 0;
				
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				
				// TODO: Compute the ray (look in Camera class)
				// map x, y to "unit" coords in [0, 1]
				double u = (x+.5)/width;
				double v = (y+.5)/height;
				
				
				
				cam.getRay(ray, u, v);
				//System.out.println("Ray: "+ray.toString());
				shadeRay(rayColor, scene, ray, scene.getLights(), 1, 1, false);
				pixelColor.set(rayColor);
				//Gamma correct and clamp pixel values
				pixelColor.gammaCorrect(2.2);
				pixelColor.clamp(0, 1);
				image.setPixelColor(pixelColor, x, y);
				
				counter++;
				float numberOfProgressDots = 20;
				if((int)(numberOfProgressDots * counter / total) != lastShownProgress) {
					lastShownProgress = (int)(numberOfProgressDots * counter / total);
					System.out.print(".");
				}
			}
		}

		// Output time
		long totalTime = (System.currentTimeMillis() - startTime);
		System.out.printf(" done in %5.2f seconds.\n", totalTime / 1000.0);

	}

	/**
	 * This method returns the color along a single ray in outColor.
	 *
	 * @param outColor output space
	 * @param scene the scene
	 * @param ray the ray to shade
	 */
	public static void shadeRay(Color outColor, Scene scene, Ray ray,// Workspace workspace, 
			ArrayList<Light> lights, int depth, double contribution, boolean internal) {
		
		
		// Reset the output color
		outColor.set(0, 0, 0);

		IntersectionRecord eyeRecord = new IntersectionRecord();
		Vector3 toEye = new Vector3();

		//System.out.println(scene.getFirstIntersection(eyeRecord, ray));
		//System.out.println(ray.toString());
		if(scene.getFirstIntersection(eyeRecord, ray)==false) {
			System.out.println("Not Intersected!");
			outColor.set(1,0,0);
			return;
		} else {
			//System.out.println(eyeRecord.toString());
			toEye.set(eyeRecord.location);
			
			
			// TODO: Compute "toEye" from eyeRecord.
			// toEye is the ray from the hit position to the eye.
			

			if (eyeRecord.surface != null) {
				outColor.set(.1,0,0);
				eyeRecord.surface.getShader().shade(outColor, scene, lights, toEye, eyeRecord);
			} else {
			}
		}
		// TODO: Find the first intersection of "ray" with the scene.
		// Record intersection in eyeRecord. If it doesn't hit anything, just return (exit function).
		// Hint: look in the Scene class for appropriate methods

		
		// TODO: FIX FOR A CAMERA OTHER THAN 0,0,0

	}

}
