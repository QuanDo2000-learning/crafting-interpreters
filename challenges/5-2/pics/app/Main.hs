module Pics where

-- needed to display the picture in the playground
import Codec.Picture

-- our line graphics programming interface
import LineGraphics

house :: Path
house = [(300, 750), (300, 450), (270, 450), (500, 200),
        (730, 450), (700, 450), (700, 750)]

door :: Path
door = [(420, 750), (420, 550), (580, 550), (580, 750)]

myLine :: Line
myLine = ((400, 400), (420, 420))

spiralRays :: Float -> Float -> Int -> Color -> Line -> Picture
spiralRays angle scaleFactor n color line
  = spiralRays' n color line
  where
    spiralRays' n color line@(p1, p2)
      | n <= 0 = []
      | otherwise = (color, [p1, p2]) : spiralRays' (n - 1) newColor newLine
      where
        newColor = if n `mod` 5 == 0
                     then fade color
                     else color
        newLine = scaleLine scaleFactor (rotateLine angle line)

rotateLine :: Float -> Line -> Line
rotateLine alpha ((x1, y1), (x2, y2))
  = ((x1, y1), (x' + x1, y' + y1))
  where
    x0 = x2 - x1
    y0 = y2 - y1
    x' = x0 * cos alpha - y0 * sin alpha
    y' = x0 * sin alpha + y0 * cos alpha

scaleLine :: Float -> Line -> Line
scaleLine factor ((x1, y1), (x2, y2))
  = ((x1, y1), (x' + x1, y' + y1))
  where
    x0 = x2 - x1
    y0 = y2 - y1
    x' = factor * x0
    y' = factor * y0

fade :: Color -> Color
fade (redC, greenC, blueC, opacityC)
  = (redC, greenC, blueC, opacityC - 1)

toBlue :: Color -> Color
toBlue (redC, greenC, blueC, opacityC)
  = (max 0 (redC - 1),
     max 0 (greenC - 2),
     min 255 (blueC + 1), 
     opacityC)

connectLine :: Line -> Line -> Line
connectLine (_, pE) line2 = startLineFrom pE line2

startLineFrom :: Point -> Line -> Line
startLineFrom startPoint@(x0, y0) ((xS, yS), (xE, yE))
  = (startPoint, ((x0 + xE - xS, y0 + yE - yS)))

spiral :: Float -> Float -> Int -> Line -> Path
spiral angle scaleFactor n line
  = spiral' n line
  where
    spiral' n line@(p1, p2)
      | n <= 0 = []
      | otherwise = p1 : spiral' (n - 1) newLine
      where
        newLine = connectLine line (scaleLine scaleFactor (rotateLine angle line))

polygon :: Int -> Line -> Path
polygon n line | n > 2 = spiral rotationAngle 1 (n + 1) line
  where
    rotationAngle = (2 * pi) / (fromIntegral n)

kochLine :: Int -> Point -> Point -> Path
kochLine n pS pE
  | n <= 0 = []
  | otherwise
  = [pS] ++ kochLine (n - 1) pS p1
         ++ kochLine (n - 1) p1 p2
         ++ kochLine (n - 1) p2 p3
         ++ kochLine (n - 1) p3 pE
         ++ [pE]
  where
    l1@(_, p1) = scaleLine (1 / 3) (pS, pE)
    l2@(_, p3) = connectLine l1 l1
    (_, p2) = rotateLine (5 / 3 * pi) $ l2

kochFlake :: Int -> Line -> Path
kochFlake n line
  = kochLine n p1 p2 ++ kochLine n p2 p3 ++ kochLine n p3 p1
  where
    [p1, p2, p3, _] = polygon 3 line

fractalTree :: Float -> Int -> Line -> Path
fractalTree factor n line = fractalTree' n line
  where
    fractalTree' 0 line = []
    fractalTree' n line
      = [p1, p4]
        ++ fractalTree' (n - 1) (p4, p5)
        ++ fractalTree' (n - 1) (p5, p3)
        ++ [p3, p2]
      where
        flipLine :: Line -> Line
        flipLine (pS, pE) = (pE, pS)

        [p1, p2, p3, p4, _] = polygon 4 line
        r = flipLine (scaleLine 0.5 (p3, p4))
        (_, p5) = rotateLine (factor * pi) r