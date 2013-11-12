package org.statismo.support.nativelibs.vtk6;

import java.awt.Toolkit;
import java.lang.reflect.Field;
import java.util.List;

import org.statismo.support.nativelibs.impl.NativeLibraryBundle;
import org.statismo.support.nativelibs.impl.NativeLibraryException;

import vtk.vtkNativeLibrary;
import vtk.vtkPanel;


public class Vtk6LibraryBundle extends NativeLibraryBundle {

	public String getName() {
		return "VTK";
	}

	public String getVersion() {
		return "6.0.0";
	}

	@Override
	protected void getSupportedPlatformsInto(List<String> list) {
		list.add(PLATFORM_LINUX64);
		list.add(PLATFORM_LINUX32);
		list.add(PLATFORM_WIN64);
		list.add(PLATFORM_WIN32);
		return;
	}

	@Override
	protected void getLibraryNamesInto(List<String> list, String platform) {

		/*
		 * NOTE: the order IS important. Later libs may depend on earlier ones
		 * being loaded, and may fail if they haven't been loaded.
		 * 
		 * The easiest "algorithm" for getting the order right is manual, using
		 * trial and error: first, add everything in random order. Then try to
		 * initialize the system. Whenever a dependency is not met, an exception
		 * will be thrown. Example:
		 * 
		 * UnsatisfiedLinkError: libvtkGeovis.so: libvtkproj4.so.5.10: cannot
		 * open shared object file: No such file or directory
		 * 
		 * ^- Which means that vtkproj4 must be moved before vtkGeovis in the
		 * list. Repeat until everything loads properly.
		 */
		
		boolean linux = platform.equals(PLATFORM_LINUX64) || platform.equals(PLATFORM_LINUX32);
		boolean windows = platform.equals(PLATFORM_WIN64) || platform.equals(PLATFORM_WIN32);

		if (windows) {
			list.add("msvcr100");
			list.add("msvcp100");
		}

		list.add("vtksys-6.0");
		list.add("vtkzlib-6.0");
		list.add("vtkjpeg-6.0");
		list.add("vtkpng-6.0");
		list.add("vtktiff-6.0");
		list.add("vtkhdf5-6.0");
		list.add("vtkhdf5_hl-6.0");
		list.add("vtkalglib-6.0");
		list.add("vtkoggtheora-6.0");
		list.add("vtkjsoncpp-6.0");
		list.add("vtklibxml2-6.0");
		list.add("vtkproj4-6.0");
		if (linux) {
			list.add("vtksqlite-6.0");
		}
		list.add("vtkverdict-6.0");
		list.add("vtkexpat-6.0");
		list.add("vtkfreetype-6.0");
		list.add("vtkftgl-6.0");
		list.add("vtkgl2ps-6.0");
		list.add("vtkCommonCore-6.0");
		list.add("vtkWrappingJava-6.0");
		list.add("vtkCommonCoreJava");
		list.add("vtkCommonMath-6.0");
		list.add("vtkCommonMathJava");
		list.add("vtkCommonMisc-6.0");
		list.add("vtkCommonMiscJava");
		list.add("vtkCommonSystem-6.0");
		list.add("vtkCommonSystemJava");
		list.add("vtkCommonTransforms-6.0");
		list.add("vtkCommonTransformsJava");
		list.add("vtkCommonDataModel-6.0");
		list.add("vtkCommonDataModelJava");
		list.add("vtkCommonColor-6.0");
		list.add("vtkCommonColorJava");
		list.add("vtkCommonComputationalGeometry-6.0");
		list.add("vtkCommonComputationalGeometryJava");
		list.add("vtkCommonExecutionModel-6.0");
		list.add("vtkCommonExecutionModelJava");
		list.add("vtkDICOMParser-6.0");
		list.add("vtkmetaio-6.0");
		list.add("vtkIOCore-6.0");
		list.add("vtkIOCoreJava");
		list.add("vtkIOLegacy-6.0");
		list.add("vtkIOLegacyJava");
		list.add("vtkIOImage-6.0");
		list.add("vtkIOImageJava");
		list.add("vtkParallelCore-6.0");
		list.add("vtkParallelCoreJava");
		list.add("vtkImagingCore-6.0");
		list.add("vtkImagingCoreJava");
		list.add("vtkImagingFourier-6.0");
		list.add("vtkImagingFourierJava");
		list.add("vtkImagingSources-6.0");
		list.add("vtkImagingSourcesJava");
		list.add("vtkImagingColor-6.0");
		list.add("vtkImagingColorJava");
		list.add("vtkImagingGeneral-6.0");
		list.add("vtkImagingGeneralJava");
		list.add("vtkImagingHybrid-6.0");
		list.add("vtkImagingHybridJava");
		list.add("vtkImagingMath-6.0");
		list.add("vtkImagingMathJava");
		list.add("vtkImagingMorphological-6.0");
		list.add("vtkImagingMorphologicalJava");
		list.add("vtkImagingStatistics-6.0");
		list.add("vtkImagingStatisticsJava");
		list.add("vtkImagingStencil-6.0");
		list.add("vtkImagingStencilJava");
		list.add("vtkFiltersCore-6.0");
		list.add("vtkFiltersCoreJava");
		list.add("vtkFiltersGeneral-6.0");
		list.add("vtkFiltersGeneralJava");
		list.add("vtkFiltersAMR-6.0");
		list.add("vtkFiltersAMRJava");
		list.add("vtkFiltersStatistics-6.0");
		list.add("vtkFiltersStatisticsJava");
		list.add("vtkFiltersExtraction-6.0");
		list.add("vtkFiltersExtractionJava");
		list.add("vtkFiltersSources-6.0");
		list.add("vtkFiltersSourcesJava");
		list.add("vtkFiltersFlowPaths-6.0");
		list.add("vtkFiltersFlowPathsJava");
		list.add("vtkFiltersGeneric-6.0");
		list.add("vtkFiltersGenericJava");
		list.add("vtkFiltersGeometry-6.0");
		list.add("vtkFiltersGeometryJava");
		list.add("vtkIOGeometry-6.0");
		list.add("vtkIOGeometryJava");
		list.add("vtkIOXMLParser-6.0");
		list.add("vtkIOXMLParserJava");
		list.add("vtkIOXML-6.0");
		list.add("vtkIOXMLJava");
		list.add("vtkIOAMR-6.0");
		list.add("vtkIOAMRJava");
		list.add("vtkIOEnSight-6.0");
		list.add("vtkIOEnSightJava");
		list.add("vtkNetCDF-6.0");
		list.add("vtkNetCDF_cxx-6.0");
		list.add("vtkexoIIc-6.0");
		list.add("vtkIOExodus-6.0");
		list.add("vtkIOExodusJava");
		list.add("vtkIOPLY-6.0");
		list.add("vtkIOPLYJava");
		list.add("vtkIOSQL-6.0");
		list.add("vtkIOSQLJava");
		list.add("vtkIOVideo-6.0");
		list.add("vtkIOVideoJava");
		list.add("vtkRenderingCore-6.0");
		list.add("vtkRenderingCoreJava");
		list.add("vtkFiltersHybrid-6.0");
		list.add("vtkFiltersHybridJava");
		list.add("vtkFiltersHyperTree-6.0");
		list.add("vtkFiltersHyperTreeJava");
		list.add("vtkFiltersImaging-6.0");
		list.add("vtkFiltersImagingJava");
		list.add("vtkFiltersModeling-6.0");
		list.add("vtkFiltersModelingJava");
		list.add("vtkFiltersParallel-6.0");
		list.add("vtkFiltersParallelJava");
		list.add("vtkFiltersParallelImaging-6.0");
		list.add("vtkFiltersParallelImagingJava");
		list.add("vtkFiltersProgrammable-6.0");
		list.add("vtkFiltersProgrammableJava");
		list.add("vtkFiltersSelection-6.0");
		list.add("vtkFiltersSelectionJava");
		list.add("vtkFiltersTexture-6.0");
		list.add("vtkFiltersTextureJava");
		list.add("vtkFiltersVerdict-6.0");
		list.add("vtkFiltersVerdictJava");
		list.add("vtkRenderingOpenGL-6.0");
		list.add("vtkRenderingOpenGLJava");
		list.add("vtkRenderingFreeType-6.0");
		list.add("vtkRenderingFreeTypeJava");
		list.add("vtkRenderingAnnotation-6.0");
		list.add("vtkRenderingAnnotationJava");
		list.add("vtkRenderingContext2D-6.0");
		list.add("vtkRenderingContext2DJava");
		list.add("vtkRenderingFreeTypeOpenGL-6.0");
		list.add("vtkRenderingFreeTypeOpenGLJava");
		list.add("vtkRenderingGL2PS-6.0");
		list.add("vtkRenderingGL2PSJava");
		list.add("vtkRenderingHybridOpenGL-6.0");
		list.add("vtkRenderingHybridOpenGLJava");
		list.add("vtkRenderingImage-6.0");
		list.add("vtkRenderingImageJava");
		list.add("vtkRenderingLabel-6.0");
		list.add("vtkRenderingLabelJava");
		list.add("vtkRenderingLOD-6.0");
		list.add("vtkRenderingLODJava");
		list.add("vtkRenderingVolume-6.0");
		list.add("vtkRenderingVolumeJava");
		list.add("vtkRenderingVolumeAMR-6.0");
		list.add("vtkRenderingVolumeAMRJava");
		list.add("vtkRenderingVolumeOpenGL-6.0");
		list.add("vtkRenderingVolumeOpenGLJava");
		list.add("vtkInfovisCore-6.0");
		list.add("vtkInfovisCoreJava");
		list.add("vtkIOExport-6.0");
		list.add("vtkIOExportJava");
		list.add("vtkIOImport-6.0");
		list.add("vtkIOImportJava");
		list.add("vtkIOInfovis-6.0");
		list.add("vtkIOInfovisJava");
		list.add("vtkIOLSDyna-6.0");
		list.add("vtkIOLSDynaJava");
		list.add("vtkIOMINC-6.0");
		list.add("vtkIOMINCJava");
		list.add("vtkIOMovie-6.0");
		list.add("vtkIOMovieJava");
		list.add("vtkIONetCDF-6.0");
		list.add("vtkIONetCDFJava");
		list.add("vtkIOParallel-6.0");
		list.add("vtkIOParallelJava");
		list.add("vtkChartsCore-6.0");
		list.add("vtkChartsCoreJava");
		list.add("vtkDomainsChemistry-6.0");
		list.add("vtkDomainsChemistryJava");
		list.add("vtkInfovisLayout-6.0");
		list.add("vtkInfovisLayoutJava");
		list.add("vtkInteractionStyle-6.0");
		list.add("vtkInteractionStyleJava");
		list.add("vtkInteractionWidgets-6.0");
		list.add("vtkInteractionWidgetsJava");
		list.add("vtkInteractionImage-6.0");
		list.add("vtkInteractionImageJava");
		list.add("vtkViewsCore-6.0");
		list.add("vtkViewsCoreJava");
		list.add("vtkViewsInfovis-6.0");
		list.add("vtkViewsInfovisJava");
		list.add("vtkGeovisCore-6.0");
		list.add("vtkGeovisCoreJava");
		list.add("vtkViewsGeovis-6.0");
		list.add("vtkViewsGeovisJava");
		list.add("vtkViewsContext2D-6.0");
		list.add("vtkViewsContext2DJava");
	}

	@Override
	protected void onInitializeStart() throws NativeLibraryException {
		// // Loads mawt.so
		Toolkit.getDefaultToolkit();
		// // Loads jawt.so - this is explicitly required in JRE 7
		try {
			System.loadLibrary("jawt");
		} catch (UnsatisfiedLinkError ignored) {}
	}

	@Override
	protected void onInitializeEnd() throws NativeLibraryException {
		try {
			Field loaded = vtkNativeLibrary.class.getDeclaredField("loaded");
			loaded.setAccessible(true);
			for (vtkNativeLibrary lib : vtkNativeLibrary.values()) {
				loaded.set(lib, Boolean.TRUE);
			}
		} catch (Throwable t) {
			throw new NativeLibraryException(
					"Unexpected error: unable to initialize internal VTK state",
					t);
		}

		new vtkPanel();
	}

	@Override
	public Runnable getVerifierRunnable() {
		return new Runnable() {
			
			@Override
			public void run() {
				new vtkPanel();
			}
		};
	}

	@Override
	public boolean isLoadByDefault() {
		return true;
	}


}
