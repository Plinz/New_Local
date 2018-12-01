export interface IImage {
    id?: number;
    name?: string;
    description?: string;
    imagePath?: string;
    image?: any;
    imageContentType?: string;
}

export class Image implements IImage {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public imagePath?: string,
        public image?: any,
        public imageContentType?: string
    ) {}
}
