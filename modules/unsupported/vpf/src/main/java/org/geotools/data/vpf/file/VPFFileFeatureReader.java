/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.vpf.file;

import java.io.IOException;
import java.util.NoSuchElementException;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentState;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * A feature reader for the VPFFile object
 *
 * @author <a href="mailto:jeff@ionicenterprise.com">Jeff Yutzler</a>
 * @source $URL$
 */
public class VPFFileFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {
    private final VPFFile file;
    private SimpleFeature currentFeature;

    /** State used when reading file */
    protected ContentState state;

    public VPFFileFeatureReader(VPFFile type) {
        file = type;
        currentFeature = null;

        file.reset();
    }

    public VPFFileFeatureReader(ContentState contentState, Query query) throws IOException {
        this.state = contentState;
        VPFFileStore vpf = (VPFFileStore) contentState.getEntry().getDataStore();
        this.file = vpf.getDefaultFile();
        this.currentFeature = null;
        this.file.reset();
        /*
        reader = csv.read(); // this may throw an IOException if it could not connect
        boolean header = reader.readHeaders();
        if (!header) {
            throw new IOException("Unable to read csv header");
        }
        builder = new SimpleFeatureBuilder(state.getFeatureType());
        geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
        row = 0;
        */
    }

    /* (non-Javadoc)
     * @see org.geotools.data.FeatureReader#getFeatureType()
     */
    public SimpleFeatureType getFeatureType() {
        return file.getFeatureType();
    }

    /* (non-Javadoc)
     * @see org.geotools.data.FeatureReader#next()
     */
    public SimpleFeature next() throws IOException, NoSuchElementException {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        currentFeature = file.readFeature();

        return currentFeature;
    }

    /* (non-Javadoc)
     * @see org.geotools.data.FeatureReader#hasNext()
     */
    public boolean hasNext() throws IOException {
        boolean result = false;

        // Ask the stream if it has space for another object
        result = file.hasNext();
        return result;
    }

    /* (non-Javadoc)
     * @see org.geotools.data.FeatureReader#close()
     */
    public void close() throws IOException {
        // TODO Auto-generated method stub
        this.file.close();
    }
}
