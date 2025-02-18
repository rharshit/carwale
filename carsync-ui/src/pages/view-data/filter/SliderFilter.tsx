import { CheckOutlined, CloseOutlined } from '@ant-design/icons';
import { Flex, Slider, Switch } from "antd";


type SliderFilterProps = {
    isFilterEnabled: boolean,
    setFilterEnabled: React.Dispatch<React.SetStateAction<boolean>>,
    lowerLimit: number,
    minValue: number,
    setMinValue: React.Dispatch<React.SetStateAction<number>>,
    higherLimit: number,
    maxValue: number,
    setMaxValue: React.Dispatch<React.SetStateAction<number>>,
}
export function SliderFilter(sliderFilterProps: SliderFilterProps) {
    const {
        isFilterEnabled,
        setFilterEnabled,
        lowerLimit,
        minValue,
        setMinValue,
        higherLimit,
        maxValue,
        setMaxValue
    } = sliderFilterProps;

    const onChangeComplete = (value: number[]) => {
        setMinValue(value[0])
        setMaxValue(value[1])
    };

    const onChange = (value: number[]) => {
        if (Number.isNaN(value)) {
            return;
        }
        setMinValue(value[0]);
        setMaxValue(value[1]);
    };

    return (
        <>
            <Flex
                gap='small'
                justify='space-around'
                align='flex-end'
                style={{ width: 600 }}>
                <Flex vertical>
                    <Switch
                        checkedChildren={<CheckOutlined />}
                        unCheckedChildren={<CloseOutlined />}
                        checked={isFilterEnabled}
                        onChange={setFilterEnabled}
                    />
                </Flex>
                <Slider
                    range
                    min={lowerLimit}
                    max={higherLimit}
                    value={[minValue, maxValue]}
                    disabled={!isFilterEnabled}
                    tooltip={{
                        open: isFilterEnabled,
                        arrow: false,
                        placement: 'bottom',
                    }}
                    onChange={onChange}
                    onChangeComplete={onChangeComplete}
                    style={{ width: 690 }}
                />
            </Flex>
        </>
    )
}
