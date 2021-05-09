from PIL import Image
from PIL import ImageFont
from PIL import ImageDraw

import argparse

parser = argparse.ArgumentParser(description='Add text')
parser.add_argument('img', type=str, help='Input dir for videos')
args = parser.parse_args()


img = Image.open(args.img)
width, height = img.size
draw = ImageDraw.Draw(img)
# font = ImageFont.truetype("sans-serif.ttf", 16)
draw.text((0, height / 2 - 20),"HELLO WORLD",(0,0,0))
img.save(args.img)

