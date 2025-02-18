import { CheckOutlined, CloseOutlined } from '@ant-design/icons';
import { Flex, Input, Slider, Switch, Typography } from "antd";
import { useEffect, useState } from 'react';

interface NumericInputProps {
    disabled: boolean
    value: string;
    onChange: (value: string) => void;
    preText?: string,
    postText?: string,
}

const NumericInput = (props: NumericInputProps) => {
    const { disabled, value, onChange, preText, postText } = props;

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { value: inputValue } = e.target;
        const reg = /^-?\d*?$/;
        if (reg.test(inputValue) || inputValue === '') {
            onChange(inputValue);
        }
    };

    const handleBlur = () => {
        onChange(value.replace(/0*(\d+)/, '$1'));
    };

    return (
        <Input
            {...props}
            style={{ width: 190 }}
            disabled={disabled}
            addonBefore={preText}
            suffix={postText}
            onChange={handleChange}
            onBlur={handleBlur}
            maxLength={16}
        />
    );
};

type SliderFilterProps = {
    name: string,
    unit?: string,
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
        name,
        unit,
        isFilterEnabled,
        setFilterEnabled,
        lowerLimit,
        minValue,
        setMinValue,
        higherLimit,
        maxValue,
        setMaxValue
    } = sliderFilterProps;

    const { Text } = Typography;

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
                gap='large'
                justify='space-around'
                align='center'
                style={{ width: 'auto', margin: '0px 0px 16px 0px' }}>
                <Flex style={{ rotate: '-90deg' }}>
                    <Switch
                        checkedChildren={<CheckOutlined style={{ rotate: '90deg' }} />}
                        unCheckedChildren={<CloseOutlined style={{ rotate: '90deg' }} />}
                        checked={isFilterEnabled}
                        onChange={setFilterEnabled}
                    />
                </Flex>
                <Flex vertical>
                    <Flex justify='space-between' align='center' style={{ width: '100%' }}>
                        <NumericInput
                            disabled={!isFilterEnabled}
                            value={minValueString}
                            onChange={setMinValueString}
                            preText='Min'
                            postText={unit}
                        />
                        <Text disabled={!isFilterEnabled} type="secondary">{"<- " + name + " ->"}</Text>
                        <NumericInput
                            disabled={!isFilterEnabled}
                            value={maxValueString}
                            onChange={setMaxValueString}
                            preText='Max'
                            postText={unit}
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
            </Flex>
        </>
    )
}
