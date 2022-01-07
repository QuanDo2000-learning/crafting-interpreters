inc :: Num a => a -> a
inc x = x + 1

double :: Num a => a -> a 
double x = 2 * x

exclaim :: String -> String
exclaim str = str ++ "!"

average :: Fractional a => a -> a -> a
average x y = (x + y) / 2