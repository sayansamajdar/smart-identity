/*
 * 
 *  Licensed to the Rhiot under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  
 */

package com.techengage.components.camel.openimaj;

/**
 * Constants Class
 */
public class OpenImajConstants {

    public static final float DEFAULT_CONFIDENCE = 10;

    public static final String OPENIMAJ_DETECTED_FACE_COUNT = "CamelOpenImajDetectedFaceCount";
    
    public static final String OPENIMAJ_IMAGE = "CamelOpenImajImage";
    
    public static final float DEFAULT_RECOGNITION_THRESHOLD = 50;//TODO review

    public static final float RECOGNITION_THRESHOLD = DEFAULT_RECOGNITION_THRESHOLD;//TODO review

    private OpenImajConstants() {
	// Constants class
    }
}
