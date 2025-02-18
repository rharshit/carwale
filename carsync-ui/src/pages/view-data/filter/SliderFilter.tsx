import { CheckOutlined, CloseOutlined } from '@ant-design/icons';
import { Flex, Input, Slider, Switch } from "antd";
import { useEffect, useState } from 'react';

interface NumericInputProps {
    style: React.CSSProperties;
    value: string;
    onChange: (value: string) => void;
}

const NumericInput = (props: NumericInputProps) => {
    const { value, onChange } = props;

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { value: inputValue } = e.target;
        const reg = /^-?\d*(\.\d*)?$/;
        if (reg.test(inputValue) || inputValue === '' || inputValue === '-') {
            onChange(inputValue);
        }
    };

    // '.' at the end or only '-' in the input box.
    const handleBlur = () => {
        let valueTemp = value;
        if (value.charAt(value.length - 1) === '.' || value === '-') {
            valueTemp = value.slice(0, -1);
        }
        onChange(valueTemp.replace(/0*(\d+)/, '$1'));
    };

    return (
        <Input
            {...props}
            onChange={handleChange}
            onBlur={handleBlur}
            maxLength={16}
        />
    );
};

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

    const [isSliderUpdating, setIsSliderUpdating] = useState<boolean>(false)
    const [minValueString, setMinValueString] = useState<string>(minValue.toString())
    const [maxValueString, setMaxValueString] = useState<string>(maxValue.toString())

    useEffect(() => {
        if (minValue.toString() != minValueString) {
            setMinValue(+minValueString)
        }
    }, [minValueString])

    useEffect(() => {
        if (maxValue.toString() != maxValueString) {
            setMaxValue(+maxValueString)
        }
    }, [maxValueString])

    useEffect(() => {
        if (!isSliderUpdating) {
            setMinValueString(minValue.toString())
        }
    }, [minValue, isSliderUpdating])

    useEffect(() => {
        if (!isSliderUpdating) {
            setMaxValueString(maxValue.toString())
        }
    }, [maxValue, isSliderUpdating])

    const onChangeComplete = (value: number[]) => {
        setIsSliderUpdating(false)
        setMinValue(value[0])
        setMaxValue(value[1])
    };

    const onChange = (value: number[]) => {
        setIsSliderUpdating(true)
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
                <Switch
                    checkedChildren={<CheckOutlined />}
                    unCheckedChildren={<CloseOutlined />}
                    checked={isFilterEnabled}
                    onChange={setFilterEnabled}
                />
                <Flex vertical>
                    <Flex justify='space-between' style={{ width: '100%' }}>
                        <NumericInput style={{ width: 120 }} value={minValueString} onChange={setMinValueString} />
                        <NumericInput style={{ width: 120 }} value={maxValueString} onChange={setMaxValueString} />
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
            </Flex>
        </>
    )
}
