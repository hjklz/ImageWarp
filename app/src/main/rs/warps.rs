#pragma version(1)
#pragma rs java_package_name(com.imagewarp.andy.imagewarp)

#define PI 3.141592653589793238462643383279502884197169399375

//adapted from transform.rs on eclass
//formulas for wave and swirl from http://eeweb.poly.edu/~yao/EL5123/lecture12_ImageWarping.pdf
//formula for bulge from http://stackoverflow.com/questions/5055625/image-warping-bulge-effect-algorithm

const uchar4 *input;
uchar4 *output;
int width;
int height;

static uchar4 getPixelAt(int, int);
void setPixelAt(int, int, uchar4);

void XXX();
void wave();
void swirl();
void bulge();

//unused, here for reference
void XXX(int x, int y) {

	int i, j;

	for(j = 0; j < height; j++) {
		for(i = 0; i < width; i++) {
			setPixelAt(i, j, getPixelAt(i, j));
		}
	}
}

void wave() {

	int i, j;
	float x;

	for(j = 0; j < height; j++) {
		for(i = 0; i < width; i++) {
		    x = i + 20 * (float) sin((float) (2 * PI * i / 30));

		    //closest pixel center would be (x +0.5) casted as int
			setPixelAt(i, j, getPixelAt((int) (x+0.5), j));
		}
	}
}

void swirl() {

	int i, j;
	float xcenter, ycenter, r, theta, x, y;

    xcenter = width/2;
    ycenter = height/2;

	for(j = 0; j < height; j++) {
		for(i = 0; i < width; i++) {
		    r = sqrt((float) (pow((float)(i - xcenter), 2) + pow((float)(j - ycenter), 2)));
            theta = PI * r / 512;

		    x = (i - xcenter) * cos(theta) + (j - ycenter) * sin(theta) + xcenter;
		    y = (-1) * (i - xcenter) * sin(theta) + (j - ycenter) * cos(theta) + ycenter;

		    //closest pixel center would be (x + 0.5) casted as int
			setPixelAt(i, j, getPixelAt((int) (x+0.5), (int) (y+0.5)));
		}
	}
}

void bulge() {

	int i, j;
	float xcenter, ycenter, r, rn, a, x, y, b, c, d, s;

    xcenter = width/2;
    ycenter = height/2;

	for(j = 0; j < height; j++) {
		for(i = 0; i < width; i++) {

            r = sqrt((float) (pow((float)(i - xcenter), 2) + pow((float)(j - ycenter), 2)));
            a = atan2((float) (j - ycenter), (float)(i - xcenter));

//		    b = 3; c = 0.6; d = 1/500;
//		    s = 1/(float)(1+exp((float)(b*(c-d*r))));

//		    rn = r*s;
            rn = pow(r, 2.5) / max(height, width);

            x = rn * cos(a) + xcenter;
            y = rn * sin(a) + ycenter;

            //closest pixel center would be (x +0.5) casted as int
            setPixelAt(i, j, getPixelAt((int) (x+0.5), (int) (y+0.5)));

		}
	}
}

//a convenience method to clamp getting pixels into the image
static uchar4 getPixelAt(int x, int y) {
	if(y>=height) y = height-1;
	if(y<0) y = 0;
	if(x>=width) x = width-1;
	if(x<0) x = 0;
	return input[y*width + x];
}

//take care of setting x,y on the 1d-array representing the bitmap
void setPixelAt(int x, int y, uchar4 pixel) {
	output[y*width + x] = pixel;
}
