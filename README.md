# Simplex-Islands
Visualization of Simplex noise function to procedurally generate 2D island maps

![Screenshot of v0.1](https://raw.githubusercontent.com/dunctait/Simplex-Islands/master/Screenshot.png)

Access the .jar [here](https://raw.githubusercontent.com/dunctait/Simplex-Islands/master/Simplex%20Islands.jar).

# About

This small program was originally written to give a better understanding of how the various settings affect the output from the Simplex Noise function. See [here](http://staffwww.itn.liu.se/~stegu/simplexnoise/simplexnoise.pdf) for the origin of the Simplex code.

I am now using the program to create random potential levels for a project I am working on so it is now leaning toward being designed for my specific use case.

# Settings

The non-Simplex functions are as follows:

Colour 1: The values are all normalized to the range 0-1 so this setting specifies which values from the array are presented by the first colour (in this case the blue "sea" colour). The default is all values from 0 to 0.21.

Colour 2: Same as above, but for the orangey "sand" colour. The default is to make all values between 0.21 to 0.27 this colour.

Colour 3: Same as above for the green "land" colour. The default is to make all values between 0.27 and 0.6 this colour.

Colour 4: This option is not actually editable as it assigns all of the remaining values to the final colour (the grey "mountain" colour).

Edge Fade Coverage: This option fades out the values to 0 at the edges of the map to ensure no land touches the edge of the window. The value selected determines what percentage of the distance from the edge to the center the fade is enabled. For instance, a value of 0.1 will linearly fade in the outer 10% of the map.

Radial Gradient Mask: This enables or disables a linear radial gradient mask that helps make the island more circular. Potential TODO is to alter this to take a variable that controls the drop off of the mask (i.e change it from being linear).

Seed: I implemented a seed function where when the Simplex function creates the noise by looking at the hardcoded array the seed value is added to each value then modded by 255. This essentially gives 256 different maps (per collection of settings).