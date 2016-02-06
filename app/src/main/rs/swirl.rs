#pragma version(1)
#pragma rs java_package_name(com.imagewarp.andy.imagewarp)

//uchar4 __attribute__((kernel)) bar(uint32_t x, uint32_t y) {
//    uchar4 ret = ((uchar) x, (uchar) y, (uchar) (x + y), (uchar)255);
//    return ret;
//}

const uchar4 *input;
uchar4 *output;
int width;
int height;

static uchar4 getPixelAt(int, int);
void setPixelAt(int, int, uchar4);
void XXX();

void XXX(int x, int y) {

	int i, j;

	for(j = 0; j < height; j++) {
		for(i = 0; i < width; i++) {

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
