import { ConfigProvider, theme } from "antd";
import { useState } from "react";
import MainLayout from "./pages/MainLayout";

function App() {

  const [isDark, setDark] = useState<boolean>(false);
  const [isCompact, setCompact] = useState<boolean>(true);
  const [isRounded, setRounded] = useState<boolean>(true);

  const getCorners = (radius: number, isRounded: boolean): number => {
    return isRounded ? radius * (isCompact ? 2 : 4) : radius;
  }

  return (
    <ConfigProvider theme={{
      algorithm: [isDark ? theme.darkAlgorithm : theme.defaultAlgorithm].concat(isCompact ? [theme.compactAlgorithm] : []),
      token: {
        colorPrimary: '#5385c6',
        borderRadius: getCorners(4, isRounded),
        borderRadiusXS: getCorners(2, isRounded),
        borderRadiusSM: getCorners(4, isRounded),
        borderRadiusLG: getCorners(8, isRounded),
        borderRadiusOuter: getCorners(4, isRounded),
        fontWeightStrong: 400
      }
    }}>
      <MainLayout
        isDark={isDark}
        setDark={setDark}
        isCompact={isCompact}
        setCompact={setCompact}
        isRounded={isRounded}
        setRounded={setRounded}
      />
    </ConfigProvider>
  )
}

export default App
