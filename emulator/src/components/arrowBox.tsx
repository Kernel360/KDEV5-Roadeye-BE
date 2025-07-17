import React from 'react';

interface ArrowBoxProps {
    children?: React.ReactNode;
    width?: string;
    height?: string;
    backgroundColor?: string;
    borderColor?: string;
    borderWidth?: string;
    borderRadius?: string;
    arrowSize?: string;
    arrowColor?: string;
    className?: string;
}

const ArrowBox: React.FC<ArrowBoxProps> = ({
    children,
    width = '100px',
    height = '100px',
    backgroundColor = '#ffffff',
    borderColor = '#333333',
    borderWidth = '2px',
    borderRadius = '8px',
    arrowSize = '20px',
    arrowColor = '#333333',
    className = '',
}) => {
    return (
        <div className={`arrow-box ${className}`} style={{ position: 'relative', display: 'inline-block' }}>
            {/* 네모난 사각형 */}
            <div
                style={{
                    width,
                    height,
                    backgroundColor,
                    border: `${borderWidth} solid ${borderColor}`,
                    borderRadius,
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    position: 'relative',
                }}
            >
                {children}
            </div>

            {/* 아래쪽 화살표 */}
            <div
                style={{
                    position: 'absolute',
                    bottom: `-${arrowSize}`,
                    left: '50%',
                    transform: 'translateX(-50%)',
                    width: 0,
                    height: 0,
                    borderLeft: `${arrowSize} solid transparent`,
                    borderRight: `${arrowSize} solid transparent`,
                    borderTop: `${arrowSize} solid ${arrowColor}`,
                }}
            />
        </div>
    );
};

export default ArrowBox;
