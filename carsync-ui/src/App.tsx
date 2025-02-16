import { ConfigProvider } from "antd";
import MainLayout from "./pages/MainLayout";

function App() {

  return (
    <ConfigProvider theme={{
      token: {
        colorPrimary: '#5385c6',
        borderRadius: 16,
        borderRadiusXS: 8,
        borderRadiusSM: 16,
        borderRadiusLG: 32,
        borderRadiusOuter: 16
      }
    }}>
      <MainLayout />
    </ConfigProvider>
  )
}

export default App
