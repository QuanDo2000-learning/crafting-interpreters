

module Examples where
  import Data.Char

  allToUpper :: String -> String
  allToUpper [] = []
  allToUpper (x : xs) = toUpper x : allToUpper xs

  natSum :: Int -> Int
  natSum 0 = 0
  natSum n
    | n > 0 = n + natSum (n - 1)
    | otherwise = error "natSum: Input value too small"