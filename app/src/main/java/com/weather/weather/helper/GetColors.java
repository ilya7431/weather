package com.weather.weather.helper;


import com.weather.weather.R;

public class GetColors {
    private long[] arrayColors;


    /**
     *
     * @param value enum TypeWeather
     * @return  going 3 color id on array long.
     */
    public long[] GetImage(TypeWeather value){

        arrayColors = new long[3];

        switch (value) {
            case Clear_Sky: {

                arrayColors[0] = (R.drawable.sun);
                arrayColors[1] = (R.color.colorClear);
                arrayColors[2] = (R.color.colorClear);


                break;
            }
            case Few_Clouds: {

                arrayColors[0] = (R.drawable.cloud);
                arrayColors[1] = (R.color.colorBrokenClouds);
                arrayColors[2] = (R.color.colorBrokenClouds);

                break;
            }
            case Scattered_Clouds: {

                arrayColors[0] = (R.drawable.cloud);
                arrayColors[1] = (R.color.colorBrokenClouds);
                arrayColors[2] = (R.color.colorBrokenClouds);

                break;
            }
            case BrokenClouds: {
                arrayColors[0] = (R.drawable.cloud);
                arrayColors[1] = (R.color.colorBrokenClouds);
                arrayColors[2] = (R.color.colorBrokenClouds);

                break;
            }
            case Shower_Rain: {
                arrayColors[0] = (R.drawable.sky);
                arrayColors[1] = (R.color.colorSky);
                arrayColors[2] = (R.color.colorSky);

                break;
            }
            case Rain: {
                arrayColors[0] = (R.drawable.sky);
                arrayColors[1] = (R.color.colorSky);
                arrayColors[2] = (R.color.colorSky);
                break;
            }
            case Thunderstorm: {

                arrayColors[0] = (R.drawable.thunderstorm_clouds);
                arrayColors[1] = (R.color.colorThunderstorm);
                arrayColors[2] = (R.color.colorThunderstorm);
                break;
            }
            case Snow: {

                arrayColors[0] = (R.drawable.weather);
                arrayColors[1] = (R.color.colorSnow);
                arrayColors[2] = (R.color.colorSnow);

                break;
            }
            default: {
                arrayColors[0] = (R.drawable.sun);
                arrayColors[1] = (R.color.colorClear);
                arrayColors[2] = (R.color.colorClear);
            }

        }
        return arrayColors;
    }


}
