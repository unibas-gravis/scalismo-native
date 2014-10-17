package org.statismo.support.nativelibs.vtk6;

import org.statismo.support.nativelibs.impl.NativeLibraryBundle;
import org.statismo.support.nativelibs.impl.NativeLibraryException;
import org.statismo.support.nativelibs.impl.Platform;
import vtk.vtkNativeLibrary;
import vtk.vtkPanel;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;


public class Vtk6LibraryBundle extends NativeLibraryBundle {

    public String getName() {
        return "VTK";
    }

    public String getVersion() {
        return "6.1.0";
    }

    @Override
    protected void getSupportedPlatformsInto(List<String> list) {
        list.add(Platform.PLATFORM_LINUX64);
        list.add(Platform.PLATFORM_LINUX32);
        list.add(Platform.PLATFORM_WIN64);
        list.add(Platform.PLATFORM_WIN32);
        list.add(Platform.PLATFORM_MAC64);
    }

    @Override
    protected void getLibraryNamesInto(List<String> list) {

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

        if (Platform.isWindows()) {
            list.add("msvcr100");
            list.add("msvcp100");
        }

        list.add("vtksys-6.1");
        list.add("vtkzlib-6.1");
        list.add("vtkjpeg-6.1");
        list.add("vtkpng-6.1");
        list.add("vtktiff-6.1");
        list.add("vtkhdf5-6.1");
        list.add("vtkhdf5_hl-6.1");
        list.add("vtkalglib-6.1");
        list.add("vtkoggtheora-6.1");
        list.add("vtkjsoncpp-6.1");
        list.add("vtklibxml2-6.1");
        list.add("vtkproj4-6.1");
        if (Platform.isLinux() || Platform.isMac()) {
            list.add("vtksqlite-6.1");
        }
        list.add("vtkverdict-6.1");
        list.add("vtkexpat-6.1");
        list.add("vtkfreetype-6.1");
        list.add("vtkftgl-6.1");
        list.add("vtkgl2ps-6.1");
        list.add("vtkCommonCore-6.1");
        list.add("vtkWrappingJava-6.1");
        list.add("vtkCommonCoreJava");
        list.add("vtkCommonMath-6.1");
        list.add("vtkCommonMathJava");
        list.add("vtkCommonMisc-6.1");
        list.add("vtkCommonMiscJava");
        list.add("vtkCommonSystem-6.1");
        list.add("vtkCommonSystemJava");
        list.add("vtkCommonTransforms-6.1");
        list.add("vtkCommonTransformsJava");
        list.add("vtkCommonDataModel-6.1");
        list.add("vtkCommonDataModelJava");
        list.add("vtkCommonColor-6.1");
        list.add("vtkCommonColorJava");
        list.add("vtkCommonComputationalGeometry-6.1");
        list.add("vtkCommonComputationalGeometryJava");
        list.add("vtkCommonExecutionModel-6.1");
        list.add("vtkCommonExecutionModelJava");
        list.add("vtkDICOMParser-6.1");
        list.add("vtkmetaio-6.1");
        list.add("vtkIOCore-6.1");
        list.add("vtkIOCoreJava");
        list.add("vtkIOLegacy-6.1");
        list.add("vtkIOLegacyJava");
        list.add("vtkIOImage-6.1");
        list.add("vtkIOImageJava");
        list.add("vtkParallelCore-6.1");
        list.add("vtkParallelCoreJava");
        list.add("vtkImagingCore-6.1");
        list.add("vtkImagingCoreJava");
        list.add("vtkImagingFourier-6.1");
        list.add("vtkImagingFourierJava");
        list.add("vtkImagingSources-6.1");
        list.add("vtkImagingSourcesJava");
        list.add("vtkImagingColor-6.1");
        list.add("vtkImagingColorJava");
        list.add("vtkImagingGeneral-6.1");
        list.add("vtkImagingGeneralJava");
        list.add("vtkImagingHybrid-6.1");
        list.add("vtkImagingHybridJava");
        list.add("vtkImagingMath-6.1");
        list.add("vtkImagingMathJava");
        list.add("vtkImagingMorphological-6.1");
        list.add("vtkImagingMorphologicalJava");
        list.add("vtkImagingStatistics-6.1");
        list.add("vtkImagingStatisticsJava");
        list.add("vtkImagingStencil-6.1");
        list.add("vtkImagingStencilJava");
        list.add("vtkFiltersCore-6.1");
        list.add("vtkFiltersCoreJava");
        list.add("vtkFiltersGeneral-6.1");
        list.add("vtkFiltersGeneralJava");
        list.add("vtkFiltersAMR-6.1");
        list.add("vtkFiltersAMRJava");
        list.add("vtkFiltersStatistics-6.1");
        list.add("vtkFiltersStatisticsJava");
        list.add("vtkFiltersExtraction-6.1");
        list.add("vtkFiltersExtractionJava");
        list.add("vtkFiltersSources-6.1");
        list.add("vtkFiltersSourcesJava");
        list.add("vtkFiltersFlowPaths-6.1");
        list.add("vtkFiltersFlowPathsJava");
        list.add("vtkFiltersGeneric-6.1");
        list.add("vtkFiltersGenericJava");
        list.add("vtkFiltersGeometry-6.1");
        list.add("vtkFiltersGeometryJava");
        list.add("vtkIOGeometry-6.1");
        list.add("vtkIOGeometryJava");
        list.add("vtkIOXMLParser-6.1");
        list.add("vtkIOXMLParserJava");
        list.add("vtkIOXML-6.1");
        list.add("vtkIOXMLJava");
        list.add("vtkIOAMR-6.1");
        list.add("vtkIOAMRJava");
        list.add("vtkIOEnSight-6.1");
        list.add("vtkIOEnSightJava");
        list.add("vtkNetCDF-6.1");
        list.add("vtkNetCDF_cxx-6.1");
        list.add("vtkexoIIc-6.1");
        list.add("vtkIOExodus-6.1");
        list.add("vtkIOExodusJava");
        list.add("vtkIOPLY-6.1");
        list.add("vtkIOPLYJava");
        list.add("vtkIOSQL-6.1");
        list.add("vtkIOSQLJava");
        list.add("vtkIOVideo-6.1");
        list.add("vtkIOVideoJava");
        list.add("vtkRenderingCore-6.1");
        list.add("vtkRenderingCoreJava");
        list.add("vtkFiltersHybrid-6.1");
        list.add("vtkFiltersHybridJava");
        list.add("vtkFiltersHyperTree-6.1");
        list.add("vtkFiltersHyperTreeJava");
        list.add("vtkFiltersImaging-6.1");
        list.add("vtkFiltersImagingJava");
        list.add("vtkFiltersModeling-6.1");
        list.add("vtkFiltersModelingJava");
        list.add("vtkFiltersParallel-6.1");
        list.add("vtkFiltersParallelJava");
        list.add("vtkFiltersParallelImaging-6.1");
        list.add("vtkFiltersParallelImagingJava");
        list.add("vtkFiltersProgrammable-6.1");
        list.add("vtkFiltersProgrammableJava");
        list.add("vtkFiltersSelection-6.1");
        list.add("vtkFiltersSelectionJava");
        list.add("vtkFiltersSMP-6.1");
        list.add("vtkFiltersSMPJava");
        list.add("vtkFiltersTexture-6.1");
        list.add("vtkFiltersTextureJava");
        list.add("vtkFiltersVerdict-6.1");
        list.add("vtkFiltersVerdictJava");
        list.add("vtkRenderingOpenGL-6.1");
        list.add("vtkRenderingOpenGLJava");
        list.add("vtkRenderingFreeType-6.1");
        list.add("vtkRenderingFreeTypeJava");
        list.add("vtkRenderingAnnotation-6.1");
        list.add("vtkRenderingAnnotationJava");
        list.add("vtkRenderingContext2D-6.1");
        list.add("vtkRenderingContext2DJava");
        list.add("vtkRenderingFreeTypeOpenGL-6.1");
        list.add("vtkRenderingFreeTypeOpenGLJava");
        list.add("vtkRenderingGL2PS-6.1");
        list.add("vtkRenderingGL2PSJava");
        list.add("vtkRenderingImage-6.1");
        list.add("vtkRenderingImageJava");
        list.add("vtkRenderingLabel-6.1");
        list.add("vtkRenderingLabelJava");
        list.add("vtkRenderingLIC-6.1");
        list.add("vtkRenderingLICJava");
        list.add("vtkRenderingLOD-6.1");
        list.add("vtkRenderingLODJava");
        list.add("vtkRenderingVolume-6.1");
        list.add("vtkRenderingVolumeJava");
        list.add("vtkRenderingVolumeAMR-6.1");
        list.add("vtkRenderingVolumeAMRJava");
        list.add("vtkRenderingVolumeOpenGL-6.1");
        list.add("vtkRenderingVolumeOpenGLJava");
        list.add("vtkInfovisCore-6.1");
        list.add("vtkInfovisCoreJava");
        list.add("vtkIOExport-6.1");
        list.add("vtkIOExportJava");
        list.add("vtkIOImport-6.1");
        list.add("vtkIOImportJava");
        list.add("vtkIOInfovis-6.1");
        list.add("vtkIOInfovisJava");
        list.add("vtkIOLSDyna-6.1");
        list.add("vtkIOLSDynaJava");
        list.add("vtkIOMINC-6.1");
        list.add("vtkIOMINCJava");
        list.add("vtkIOMovie-6.1");
        list.add("vtkIOMovieJava");
        list.add("vtkIONetCDF-6.1");
        list.add("vtkIONetCDFJava");
        list.add("vtkIOParallel-6.1");
        list.add("vtkIOParallelJava");
        list.add("vtkChartsCore-6.1");
        list.add("vtkChartsCoreJava");
        list.add("vtkDomainsChemistry-6.1");
        list.add("vtkDomainsChemistryJava");
        list.add("vtkInfovisLayout-6.1");
        list.add("vtkInfovisLayoutJava");
        list.add("vtkInteractionStyle-6.1");
        list.add("vtkInteractionStyleJava");
        list.add("vtkInteractionWidgets-6.1");
        list.add("vtkInteractionWidgetsJava");
        list.add("vtkInteractionImage-6.1");
        list.add("vtkInteractionImageJava");
        list.add("vtkViewsCore-6.1");
        list.add("vtkViewsCoreJava");
        list.add("vtkViewsInfovis-6.1");
        list.add("vtkViewsInfovisJava");
        list.add("vtkGeovisCore-6.1");
        list.add("vtkGeovisCoreJava");
        list.add("vtkViewsGeovis-6.1");
        list.add("vtkViewsGeovisJava");
        list.add("vtkViewsContext2D-6.1");
        list.add("vtkViewsContext2DJava");
    }

    @Override
    protected void onInitializeStart() throws NativeLibraryException {
        // // Loads mawt.so
        Toolkit.getDefaultToolkit();
        // // Loads jawt.so - this is explicitly required in JRE 7
        try {
            System.loadLibrary("jawt");
        } catch (UnsatisfiedLinkError ignored) {
        }
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
        new vtkPanel(); // should work without throwing exceptions.
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
